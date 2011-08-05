// get all gists (of a certain user) with a description containing a specific term
// a rudimentary example on how to use the interface to the gist cloud, hosted at github.
// if the following fails for you, please check if you have to update wslib's curl method by the one in 
//	json/extString-curl.scd


// 2011 Till Bovermann
// http://tangibleauditoryinterfaces.de 
// http://tai-studio.org 
// http://LFSaw.de 


a = Gist.allGistsFor("LFSaw", q.username, q.password).select{|gist|
	gist.description.contains("#scTip")
}

// print all descriptions
a.do(_.prettyprint)


// get content for one
b = a.first.pull

(
// post content of first .scd file
var title;

title = b.filenames.detect{|name| name.asString.contains(".scd")};
b.files[title].content.postcs; ""
)

(
// open all .scd files as new documents

b.filenames.select{|name| name.asString.contains(".scd")}.do{|title|
	Document.new("Gist(%): %".format(b.id, title), "// Gist(%)\n// by %\n// \"%\"\n// filename: %\n\n%".format(b.id, b.user.login, b.description, title, b.files[title].content)).background_(Color(0.9, 0.9, 0.9, 1.0)).syntaxColorize
}
)