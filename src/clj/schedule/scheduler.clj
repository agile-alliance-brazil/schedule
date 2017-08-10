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
        [:unassigned-sessions-component {:v-bind:sessions "slots['unassigned']" :v-bind:slot-size-in-minutes "slotSizeInMinutes"}]
        [:section.days
          [:day-component {:v-for "day in days" ::key "day.date" :v-bind:rooms "rooms" :v-bind:day "day" :v-bind:slots "slots[day.date.toUTCString()]"}]]
      ]
      [:script#day-template {:type "text/x-template"}
        [:table.day
          [:thead
            [:th.date "{{date}}"]
            [:th.room {:v-for "room in rooms"} "{{room}}"]
          ]
          [:tbody
            [:tr.slots {:v-for "time in slots.timeslots"}
              [:th.slot "{{time}}"]
              [:td {:v-for "room in rooms" ::key "room"}
                [:draggable.slotSessions {:v-model "slots[room][time]" :element "ul" :v-bind:options "options"}
                  [:session-component {:v-for "session in slots[room][time]" ::key "session.id" :v-bind:session "session"}]
                ]
              ]
            ]
          ]
        ]
      ]
      [:script#unassigned-sessions-template {:type "text/x-template"}
        [:section.unassigned
          [:h2 "Unassigned"]
          [:draggable.sessions {:v-model "sessions" :element "ul" :v-bind:options "options"}
            [:li {:v-for "session in sessions" ::key "session.id" :v-bind:data-duration "slotspan(session)" }
              [:session-component {:v-bind:session "session"}]
            ]
          ]
        ]
      ]
      [:script#session-template {:type "text/x-template"}
        [:div.session.item
          [:span.id "{{session.id}}"]
          [:h3.title "{{session.title}}"]
          [:p.authors "{{authors}}"]
          [:span.track "{{session.track}}"]
          [:span.audienceLevel "{{session.audienceLevel}}"]
        ]
      ]

      (map page/include-js ["https://unpkg.com/sortablejs" "https://unpkg.com/vue" "https://unpkg.com/vuedraggable"])
      (map page/include-js (link/bundle-paths request ["scheduler.js"]))]))
