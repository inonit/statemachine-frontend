(ns statemachine.event.wd
  (:require [re-frame.core :as rf]))

(rf/reg-event-db :wd/status (fn [db [_ status]]
                              (assoc db :wd/status status)))

