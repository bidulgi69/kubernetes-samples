#	build applications
build-jar:
	./gradlew :api:build && ./gradlew :microservices:product-composite:build && \
	./gradlew :microservices:product-service:build && ./gradlew :microservices:review-service:build

#	build docker image from jar
build-image:
	docker-compose build

#	fire up databases and apache kafka
gen-image:
	docker-compose up -d mongodb-0 mongodb-1 zookeeper kafka

#	generate manifests
gen-helm:
	for f in helm/components/*; do helm dependency update $$f; done && \
	helm dependency update helm/environments/prod-env

#	deploy
install-helm:
	helm install prod-env \
	helm/environments/prod-env \
	-n dove-ns \
	--create-namespace

#	cleanup
clean:
	helm delete prod-env && \
	kubectl delete ns dove-ns && \
	docker-compose down --remove-orphans && \
	sleep 2 && \
	docker rmi $$(docker images | grep dove-kube) && \
	docker volume rm $$(docker volume ls -qf dangling=true)
