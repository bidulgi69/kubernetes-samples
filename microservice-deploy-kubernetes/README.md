# Sample 1
This project is an example of deploying a spring boot microservices to a Kubernetes environment.<br>
<br>

# Structure
<img src="https://user-images.githubusercontent.com/17774927/182391185-967e3333-b57a-407a-8d18-2ae9bda27f54.png">


# Usage
1. Run minikube on local
    
        # If you are using Mac with Intel, you can use hyperkit as a driver.
        minikube start \
        --profile=profile-name-you-want \
        --memory= \
        --cpus= \
        --disk-size= \
        --driver=docker (Windows, M1 Silicon)
        
        minikube profile profile-name-you-want
        
2. Set namespace
      
        # Namespace 'dove-ns' will be generated after deploying helm
        kubectl config set-context $(kubectl config current-context) --namespace=dove-ns

3. To use locally built docker images in minikube, execute the below command.

        eval $(minikube docker-env)
    
4. Build applications and docker images
    
        make build-jar
        make build-image
        
5. Fire up databases and kafka using docker-compose

        make gen-image
    
6. Generate manifests

        make gen-helm

7. Deploy to kubernetes

        make install-helm
        # verify pods are ready
        kubectl get pods --watch -n dove-ns

8. Generate nodePort to access product-composite service

        # do not close the terminal where the command is being executed.
        
        # 1. using port-forward
        # you can access to product-composite pod through the external-ip
        kubectl port-forward service/product-composite external-ip:80
        
        # 2. using minikube service
        # you can access to product-composite pod through http://127.0.0.1:52169
        minikube service product-composite -n dove-ns
        |-----------|-------------------|-------------|------------------------|
        | NAMESPACE |       NAME        | TARGET PORT |          URL           |
        |-----------|-------------------|-------------|------------------------|
        | dove-ns   | product-composite |             | http://127.0.0.1:52169 |
        |-----------|-------------------|-------------|------------------------|
        
9. Cleanup

        make clean
        
# Apis
> Insert

    curl -XPOST http://ip:port/product-composite -H "Content-Type: application/json" -d'
      {
        "product": {
          "name": "Water ball",
          "productId": 1,
          "cost": 22
        },
        "reviews": [
          {
            "reviewId": 1,
            "author": "Joe",
            "subject": "Review...1",
            "content": "It was good to me."
          },
          {
            "reviewId": 2,
            "author": "Robin",
            "subject": "Review...2",
            "content": "It sucks to me."
          }
        ]
      }
    '
    
> Read

    curl -XGET http://ip:port/product-composite/1 
    
> Delete

    curl -XDELETE http://localhost:8443/product-composite/1
