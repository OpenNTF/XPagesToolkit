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
	rssSource = new Array("OpenNTF-Blog|http://blog.openntf.org/blogs/openntf.nsf/stories.xml","CollaborationToday - Recent|http://collaborationtoday.info/ct.nsf/feed.xsp?filter=all")

}