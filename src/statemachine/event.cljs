(ns statemachine.event
  (:require [com.stuartsierra.component :as component]
            [clojure.core.async :refer [<!] :refer-macros [go-loop]]
            [re-frame.core :as rf]
            [statemachine.db :as db]
            [statemachine.event.wd]
            [taoensso.timbre :as log]))


(rf/reg-event-db ::initialize-db (fn [_ _]
                                   db/default-db))

;; db: we shouldn't get a nil value as dispatch. log it as an error and fix
(rf/reg-event-db nil (fn [db & args]
                       (log/error args)
                       db))



(defn handle-messages [c-data-in]
  (go-loop []
    (when-let [msg (<! c-data-in)]
      (rf/dispatch [:wd/status msg])
      (recur))))

(defrecord EventManager [io started?]
  component/Lifecycle
  (start [this]
    (if started?
      this
      (do (log/info "Starting EventManager")
          (rf/dispatch-sync [::initialize-db])
          (handle-messages (:c-data-in io))
          (assoc this
                 :started? true))))
  (stop [this]
    (if-not started?
      this
      (do (log/info "Stopping EventManager")
          (assoc this
                 :started? false)))))

(defn event-manager [settings]
  (map->EventManager settings))
