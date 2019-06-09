(ns statemachine.communication
  "Communication abstraction. Abstracts away the details of the fetch API"
  (:require [clojure.string :as str]
            [taoensso.timbre :as log]))




;; fetch API
(def fetch (.-fetch js/window))

;; multimethod for keys in query-string. should always return a string
(defmulti get-key type)
(defmethod get-key js/String [k]
  k)
(defmethod get-key cljs.core/Keyword [k]
  (let [-ns (namespace k)]
    (if (str/blank? -ns)
      (name k)
      (str -ns "/" (name k)))))
(defmethod get-key :default [k]
  k)

(defn query-string [params]
  ;; thread the params last
  (->> params
       ;; reduce the params into their key/value pairs and return a new vector [k "=" v]
       ;; k uses the get-key so that we always have a valid string
       (reduce (fn [out [k v]] (conj out [(get-key k) "=" v])) [])
       ;; interpose & in the event we have more than one key/value pair
       (interpose "&")
       ;; flatten everything into one long list
       (flatten)
       ;; apply str to all the elements in the list and return the final list of our query-string
       (apply str)))

(defn request*
  [url {:keys [request-method handler params token]
        :or {request-method :get}
        :as opts}]
  (assert (#{:get :post} request-method) (str "invalid request method " request-method))
  (let [url* (str url
                  (when (= request-method :get)
                    (str "?" (query-string params))))]
    (-> url*
        (js/fetch (clj->js (merge {:method ({:post "POST" :get "GET"} request-method)
                                   :headers (merge
                                             ;; authenticate with a token?
                                             (if-not (str/blank? token)
                                               {"Authorization" (str "JWT " token)})
                                             (if (= request-method :post)
                                               {"Accept" "application/json"
                                                "Content-Type" "application/json"}
                                               {"Accept" "application/json"}))}
                                  (when (= request-method :post)
                                    {:body (.stringify js/JSON (clj->js params))}))))
        (.then (fn [response]
                 ;; chain the text in the response
                 (.text response)))
        (.then (fn [text]
                 ;; parse the text into json, then turn it into EDN
                 (handler (js->clj (.parse js/JSON text) :keywordize-keys true))))
        (.catch (fn [err]
                  (log/error "An error occurred when trying to communicate with the server:" {:error err
                                                                                              :url url
                                                                                              :params params}))))))

(defn GET [url opts]
  (request* url (merge opts {:request-method :get})))

(defn POST [url opts]
  (request* url (merge opts {:request-method :post})))
