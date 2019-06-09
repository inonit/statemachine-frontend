(ns statemachine.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub :wd/status (fn [db _]
                         (:wd/status db)))
