(ns statemachine.config)

(def config (atom {:io {:mqtt {:uri "ws://127.0.0.1:9001"
                               :opts {}}}}))

(defn is? [path value]
  (= value (get-in @config path ::not-found)))
