enyo.kind({
    name: "TweetApp",
    kind: enyo.Control,
    components: [
        { tag: "button", content: "Add Tweet", ontap: "addTweet" },
        { tag: "div", name: "tweetList" }
    ],

    nextTweetNumber: 1,
    addTweet: function(inSource, inEvent) {
        this.createComponent({
            kind: Tweet,
            container: this.$.tweetList,
            icon: "touchhead_sq_normal.jpg",
            handle: "unwiredben",
            text: "A new tweet! #" + this.nextTweetNumber
        });
        ++this.nextTweetNumber;
        this.$.tweetList.render();
    }
});