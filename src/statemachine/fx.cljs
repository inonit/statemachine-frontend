(ns statemachine.fx
  "Handles side-effects"
  (:require [re-frame.core :as rf]
            [statemachine.communication :as communication]
            [statemachine.user :refer [token]]))




(rf/reg-event-fx :data/pull (fn [co [_ uri params chained]]
                              (communication/GET (str "http://127.0.0.1:8000" uri)
                                                 (merge {:params params
                                                         :token @token}
                                                        (when chained
                                                          {:handler (fn [data]
                                                                      (rf/dispatch [chained data]))})))
                              nil))

(rf/reg-event-fx :data/push (fn [co [_ uri params chained]]
                              (communication/POST (str "http://127.0.0.1:8000" uri)
                                                  (merge {:params params
                                                          :token @token}
                                                         (when chained
                                                           {:handler (fn [data]
                                                                       (rf/dispatch [chained data]))})))
                              nil))

(rf/reg-event-fx :user/login (fn [{:keys [db] :as co} [_ data]]
                               (when (and data
                                          (:token data))
                                 (do (reset! token (:token data))
                                     {:db (assoc db :user/status :logged-in)
                                      :dispatch [:data/populate [["/api/dating/profiles/list" {} :data/profiles]
                                                                 ["/api/dating/likes/list" {} :data/likes]
                                                                 ["/api/dating/messages/list" {} :data/messages]
                                                                 ["/api/dating/user/profile" {} :user/profile]]]}))))

(rf/reg-event-fx :data/populate (fn [_ [_ populate-options]]
                                  (doseq [[uri params chained] populate-options]
                                    (communication/GET (str "http://127.0.0.1:8000" uri)
                                                       {:params params
                                                        :token @token
                                                        :handler (fn [data]
                                                                   (when chained
                                                                     (rf/dispatch [chained data])))}))
                                  nil))
