all:
	sed -e '/INSERT TABLE AFTER HERE/,$$d' README.adoc > j
	echo '// INSERT TABLE AFTER HERE - github do not allow include:: in files for obv. sec. reasons' >> j
	mklist >> j
	echo '|===========' >> j
	mv j README.adoc

clean:
	rm -f *.html
