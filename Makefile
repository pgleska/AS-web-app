app:
	rm -rf ./build
	gradle build
	cp -f ./build/libs/TournamentApp-0.0.1.war ./docker/backend/TournamentApp.war
