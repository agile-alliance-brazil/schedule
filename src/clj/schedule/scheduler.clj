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
          [:day-component {:v-bind:rooms "rooms" :v-bind:day "day"}]]
        [:unassigned-sessions-component {:v-bind:sessions "sessions"}]
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
      [:script#unassigned-sessions-template {:type "text/x-template"}
        [:section
          [:h2 "Unassigned"]
          [:ul
            [:li {:v-for "session in sessions" ::key "session.id"}
              [:session-component {:v-bind:session "session"}]
            ]
          ]
        ]
      ]
      [:script#session-template {:type "text/x-template"}
        [:div.session
          [:h3 "{{session.title}}"]
          [:p "{{authors}}"]
          [:span "{{session.track}}"]
          [:span "{{session.audienceLevel}}"]
        ]
      ]
      (page/include-js "https://unpkg.com/vue")
      (map page/include-js (link/bundle-paths request ["scheduler.js"]))]))
