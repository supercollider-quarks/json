// write your tip into a separate document; 
// first line should be a line-comment ("// " ++ description ++ "#scTip") such as
// I am a dscription #scTip

q = ();

q.content = Document.allDocuments.detect{|doc| (doc.string.split($\n).first.contains("#scTip")) && {doc != Document.current && {doc.isListener.not}}}.string;


(
var username = "LFSaw";
var password = "gnugit";

var content, description, contentDict;
var isPublic = true;


content = q.content.split($\n);
description = content.first[3..];
content = content[1..].join($\n);

// push as a new gist
contentDict = (
	'tip.scd': content
).postln;

g = Gist.createAndPush(description, contentDict, isPublic, username, password);

)

// look at it in the web browser
g.html_url.openInFinder

