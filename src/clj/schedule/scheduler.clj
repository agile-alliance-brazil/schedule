(ns schedule.scheduler
  (:require
    [hiccup.page :as page]
    [optimus.link :as link]))

(defn render-view [request]
  (page/html5
    {:lang "pt-BR"}
    [:head
      [:meta{:http-equiv "Content-Type" :content "text/html; charset=UTF-8" :charset "utf-8"}]
      (map page/include-css (link/bundle-paths request ["scheduler.css"]))]
    [:body {}
      [:main#app
        [:div {:v-for "day in days" ::key "day.date"}
          [:day-template {:v-bind:rooms "rooms" :v-bind:day "day"}]]
      ]
      [:script#day-template {:type "text/x-template"}
        [:table
          [:thead
            [:th "{{date}}"]
            [:th {:v-for "room in rooms"} "{{room}}"]
          ]
          [:tbody
            [:tr {:v-for "slot in timeslots"}
              [:td "{{slot}}"]
              [:td {:v-for "room in rooms"}]]
          ]
        ]
      ]
      (page/include-js "https://unpkg.com/vue")
      (map page/include-js (link/bundle-paths request ["scheduler.js"]))]))
