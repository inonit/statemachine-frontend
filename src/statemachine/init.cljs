(ns statemachine.init
  (:require [com.stuartsierra.component :as component]
            [day8.re-frame-10x :as rf10x]
            [re-frame.core :as rf]
            [taoensso.timbre :as log]
            [statemachine.communication]
            [statemachine.config :as config]
            [statemachine.event :as event]
            [statemachine.fx]
            [statemachine.io :as io]
            [statemachine.subs]))



(defonce -system (atom nil))

(defrecord InitManager [io started?]
  component/Lifecycle
  (start [this]
    (if started?
      this
      (do (log/info "Starting InitManager")
          (rf10x/inject-devtools!)
          (assoc this
                 :started? true))))
  (stop [this]
    (if-not started?
      this
      (do (log/info "Stopping InitManager")
          (assoc this
                 :started? false)))))

(defn init-manager [settings]
  (map->InitManager settings))

(defn- system-map []
  (component/system-map
   :init (component/using (init-manager (:init @config/config))
                          [:io])
   :event-manager (component/using (event/event-manager (:event-manager @config/config))
                                   [:io])
   :io (io/io-manager (:io @config/config))))

(defn init []
  (reset! -system (component/start (system-map))))
