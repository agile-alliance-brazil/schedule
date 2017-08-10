let sessionComponent = Vue.component('session-component', {
  template: '#session-template',
  props: {
    session: Object
  },
  computed: {
    authors: function() {
      return this.session.authors.join(' & ');
    }
  }
});

let unassignedSessionsComponent = Vue.component('unassigned-sessions-component', {
  template: '#unassigned-sessions-template',
  props: {
    sessions: Array,
    slotSizeInMinutes: Number
  },
  computed: {
    options: function() {
      return {group: "sessions"};
    }
  },
  methods: {
    slotspan: function(session) {
      return Math.ceil(session.duration / this.slotSizeInMinutes);
    }
  }
});

let dayComponent = Vue.component('day-component', {
  template: '#day-template',
  props: {
    day: Object,
    rooms: Array,
    slots: Object
  },
  computed: {
    date: function() {
      return this.day.date.toLocaleDateString(['pt-BR']);
    },
    options: function() {
      return {group: "sessions"};
    }
  }
});

let app = new Vue({
  el: '#app',
  components: {
    "day-component": dayComponent,
    "unassigned-sessions-component": unassignedSessionsComponent,
    "session-component": sessionComponent
  },
  data: {
    slotSizeInMinutes: 30,
    rooms: [
      "Sala 1",
      "Sala 2",
      "Sala 3",
      "Sala 4",
      "Sala 5",
      "Sala 6"
    ],
    days: [
      {
        date: new Date(2017, 8, 13),
        startTimeInMinutes: 540,
        endTimeInMinutes: 1080
      },
      {
        date: new Date(2017, 8, 14),
        startTimeInMinutes: 540,
        endTimeInMinutes: 1080
      },
      {
        date: new Date(2017, 8, 15),
        startTimeInMinutes: 540,
        endTimeInMinutes: 1080
      }
    ],
    sessions: [
      {
        id: 1,
        title: "lalala",
        authors: ["Ceci", "Hugo"],
        track: "software",
        audienceLevel: "advanced",
        sessionType: "workshop",
        duration: 50,
      },
      {
        id: 2,
        title: "lilili",
        authors: ["VH", "Giovanni"],
        track: "guerigueri",
        audienceLevel: "master",
        sessionType: "talk",
        duration: 25,
      }
    ],
    slots: {}
  },
  mounted: function() {
    this.slots = this.buildSlots()
  },
  methods: {
    buildSlots: function() {
      let sessionsByTimeByRoomByDays = this.days.reduce((s, day) => {
        s[day.date.toUTCString()] = this.rooms.reduce((d, room) => {
          let timeslots = this.timeSlotsFor(day);
          d.timeslots = timeslots;
          
          d[room] = timeslots.reduce((r, time) => {
            r[time] = this.sessions.filter((session) => {
              return session.room === room && session.day === day && session.time === time;
            });

            return r;
          }, {});

          return d;
        }, {});

        return s;
      }, {});

      sessionsByTimeByRoomByDays['unassigned'] = this.sessions.filter((session) => {
        return typeof(session.room) === 'undefined' || typeof(session.day) === 'undefined' || typeof(session.time) === 'undefined';
      });

      return sessionsByTimeByRoomByDays;
    },
    timeSlotsFor: function(day) {
      let startTime, endTime;
      startTime = day.startTimeInMinutes;
      endTime = day.endTimeInMinutes;
      let slotSize = this.slotSizeInMinutes;
      totalTimeslots = (endTime - startTime) / slotSize;

      let timeslotsData = [];
      let lastTimeslot = startTime;
      for (let i = 0; i < totalTimeslots; i++) {
        let hours = this.pad(Math.floor(lastTimeslot / 60), 2);
        let minutes = this.pad(lastTimeslot % 60, 2);
        let time = `${hours}:${minutes}`
        timeslotsData.push(time);
        lastTimeslot = lastTimeslot + slotSize;
      }
      return timeslotsData;
    },
    pad: function(num, size) {
      let s = num+"";
      while (s.length < size) s = "0" + s;
      return s;
    }
  }
});
