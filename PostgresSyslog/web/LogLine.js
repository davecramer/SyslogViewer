enyo.kind({
    name: "LogLine",
    kind: enyo.Control,
    tag: "div",
    style: "border-style: solid; border-width: 2px; " +
            "padding: 10px; margin: 10px; min-height: 50px",

    published: {
        id: '',
        date: '',
        facility: '',
        level: "",
        msg: ""
    },

    components: [
        { tag: "b", name: "id" },
        { tag: "span", name: "date" },
        { tag: "span", name: "facility" },
        { tag: "span", name: "level" },
        { tag: "span", name: "msg" }
    ],

    create: function() {
        this.inherited(arguments);
        this.idChanged();
        this.dateChanged();
        this.facilityChanged();
        this.levelChanged();
        this.msgChanged();
    },


    idChanged: function() {
        this.$.id.setContent(this.id );
    },

    dateChanged: function() {
        this.$.date.setContent(this.date);
    },
    facilityChanged: function() {
        this.$.facility.setContent(this.facility);
    },
    levelChanged: function() {
        this.$.level.setContent(this.level);
    },
    msgChanged: function() {
        this.$.msg.setContent(this.msg);
    }

});