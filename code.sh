git log --format='%aN' | sort -u | while read name; do echo -en "$name\t"; git log --author="$name" --pretty=tformat: --numstat --since="2022-02-28" --before="2022-08-14" -- '*.java' '*.py' '*.bpmn20.xml' '*.yaml' 'pom.xml' 'application.properties' '.sh' | awk '{ add += $1; subs += $2; loc += $1 - $2 } END { printf "added lines: %s, removed lines: %s, total lines: %s\n", add, subs, loc }' -; done

#git log --author=TheGreatAutomata --pretty=tformat: --numstat | awk '{ add += $1; subs += $2; loc += $1 - $2 } END { printf "added lines: %s, removed lines: %s, total lines: %s\n", add, subs, loc }'

