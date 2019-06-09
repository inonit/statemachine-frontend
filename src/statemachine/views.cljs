(ns statemachine.views
  (:require [clojure.core.match :refer-macros [match]]
            [re-frame.core :as rf]
            [re-com.core :as rc]
            [statemachine.config]
            [statemachine.subs]))


(defn show-status [status]
  [:<>
   [rc/title :label (str "Current state - " (:machine.state status)) :level :level2]
   (match [(:machine.state status)]
          ["take_over_the_world"] [:img.wd {:src "/img/state/take_over_the_world.jpg"}]
          ["motivated"] [:div
                         [:img.wd {:src "/img/state/motivated.jpg"}]
                         [:h2 "Why bother?"]]
          ["train"] [:div
                     [:img.wd {:src "/img/state/train.jpg"}]
                     [:h2 "Pump the iron with your personal trainer"]]
          ["accept"] [:img.wd {:src "/img/state/accept.jpeg"}]
          ["process"] [:img.wd {:src "/img/state/process.jpg"}]
          ["decline"] [:img.wd {:src "/img/state/decline.jpg"}]
          ["assess"] [:div
                      [:img.wd {:src "/img/state/assess.jpg"}]
                      [:h2 "Do we have enough members to take over the world?"]]
          ["looking_for_recruits"] [:img.wd {:src "/img/state/looking_for_recruits.jpeg"}]
          [_] [:div (str status)])])

(defn get-image-name [n]
  (case n
    "Brain" "TheBrain.png"
    "Pinky" "pinky.png"
    "Optimus Prime" "optimus.png"
    "Lotso" "lotso.png"
    "Jangles the Clown" "jangles.png"
    "Megatron" "megatron.jpg"
    "Syndrome" "syndrome.png"
    "Troll" "troll.png"
    "Ship computer" "wall-e.png"
    "Charles F. Muntz" "charsmuntz.png"
    "ohnoes.jpg"))

(defn show-applicant [status]
  (if (and (:recruit.name status)
           (not= (:machine.state status) "assess")
           (not= (:machine.state status) "take_over_the_world"))
    [:<>
     [rc/title :label (str "Applicant - " (:recruit.name status)) :level :level2]
     [:img.wd {:src (str "/img/applicants/" (get-image-name (:recruit.name status)))}]
     [rc/label :label (str "Current strength is " (:recruit.strength status))]
     [rc/label :label (str "Alignment is " (:recruit.alignment status))]]))

(defn index []
  (let [status (rf/subscribe [:wd/status])]
    (fn []
      (let [s @status]
        [rc/v-box
         :margin "3em"
         :gap "1em"
         :align :center
         :children [[rc/title :label "World dominiation for dummies" :level :level1]
                    [:img {:src "/img/world-domination.jpg" :style {:width "543px" :height "399px"}}]
                    [show-applicant s]
                    [show-status s]]]
       ))))
