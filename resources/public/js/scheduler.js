let dayComponent = Vue.component('day-component', {
  template: '#day-template',
  props: {
    day: Object,
    rooms: Array,
  },
  computed: {
    date: function() {
      return this.day.date.toLocaleDateString(['pt-BR']);
    },
    timeslots: function() {
      let startTime, endTime;
      startTime = this.day.startTimeInMinutes;
      endTime = this.day.endTimeInMinutes;
      let slotSize = this.day.slotSizeInMinutes;
      totalTimeslots = (endTime - startTime) / slotSize;
      
      let timeslots = [];
      let lastTimeslot = startTime;
      for (let i = 0; i < totalTimeslots; i++) {
        let hours = this.pad(Math.floor(lastTimeslot / 60), 2);
        let minutes = this.pad(lastTimeslot % 60, 2);
        timeslots.push(`${hours}:${minutes}`)
        lastTimeslot = lastTimeslot + slotSize;
      }
      return timeslots;
    }
  },
  methods: {
    pad: function(num, size) {
      let s = num+"";
      while (s.length < size) s = "0" + s;
      return s;
    }
  }
})

let app = new Vue({
  el: '#app',
  components: {
    "day-template": dayComponent
  },
  data: {
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
        endTimeInMinutes: 1080,
        slotSizeInMinutes: 30,
      },
      {
        date: new Date(2017, 8, 14),
        startTimeInMinutes: 540,
        endTimeInMinutes: 1080,
        slotSizeInMinutes: 30,
      },
      {
        date: new Date(2017, 8, 15),
        startTimeInMinutes: 540,
        endTimeInMinutes: 1080,
        slotSizeInMinutes: 30,
      }
    ]
  }
})
