var rssSource = null;
function getSources () {
	if (rssSource == null) {
		_initRSSArray()
	}
	return rssSource;
}

function getFirstSource() {
	if (rssSource == null) {
		_initRSSArray()
	}
	var arrURL =rssSource[0].split("|"); 
	return arrURL[1];
}

function _initRSSArray() {
	rssSource = new Array("OpenNTF-Blog|http://blog.openntf.org/blogs/openntf.nsf/stories.xml",
			"CollaborationToday - Recent|http://collaborationtoday.info/ct.nsf/feed.xsp?filter=all",
			"CollaborationToday - Popular|http://collaborationtoday.info/ct.nsf/feed.xsp?filter=popular",
			"WebGate Consulting AG - News|http://www.webgate.biz/WGC/shared.nsf/rss-feeds?OpenAgent&newsgroup=WGC-News-DE",
			"WebGate Consulting AG - Blog|http://www.webgate.biz/WGC/wgcblog.nsf/feed.rss",
			"GuedeByte - The Blog|http://guedebyte.wordpress.com/feed/")

}