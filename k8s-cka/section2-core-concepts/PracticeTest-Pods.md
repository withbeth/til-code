# Practice - 22 ~ 28 PODs

## Q1
How many pods in the default name space

```shell
$ kubectl get pods
$ kubectl get pods -A
```

## Q2
Create a new pod with the nginx image.

```shell
$ kubectl run {podName} --image={imageName}
$ kubectl run nginx --image=nginx
```

## Q3
What is the image used to create the new pods?
You must look at one of the new pods in detail to figure this out.

```shell
$ kubectl describe -h
$ kubectl describe TYPE NAME_PREFIX
$ kubectl describe pod newpods
```

## Q4
Which nodes are these pods placed on?
You must look at all the pods in detail to figure this out.

```shell
$ kubectl describe pod newpods 
and check out node fields info
```

## Q5
How many containers are part of the pod webapp?
Note: We just created a new POD. Ignore the state of the POD for now.

```shell
$ kubectl describe pod webapp
and check out how many container id in containers field info
```

## Q6
What images are used in the new webapp pod?
You must look at all the pods in detail to figure this out.

```shell
$ kubectl describe pod webapp
and check out image field
```

## Q7
What is the state of the container agentx in the pod webapp?
Wait for it to finish the ContainerCreating state

```shell
$ kubectl describe pod webapp
and check Events field
```

## Q8
What does the READY column in the output of the kubectl get pods command indicate?

```shell
# Running Containers / Total Containers in POD
$ kubectl get pods
```


## Q9
Delete the webapp Pod.
Once deleted, wait for the pod to fully terminate.

```shell
$ kubectl delete -h
$ kubectl delete pod webapp
```

## Q10
Create a new pod with the name redis and the image redis123.
Use a pod-definition YAML file. And yes the image name is wrong!

`$ kubectl create -f pod-redis.yaml`

- 필수항목은 4개

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: redis
spec:
  containers:
  - name: redis
    image : redis123
```

```shell
controlplane ~ ➜  kubectl create -f sample3.yaml
pod/redis created
```

## Q Final
Now change the image on this pod to redis.
Once done, the pod should be in a running state.

```bash
controlplane ~ ➜  kubectl apply -f sample3.yaml 
Warning: resource pods/redis is missing the kubectl.kubernetes.io/last-applied-configuration annotation which is required by kubectl apply. kubectl apply should only be used on resources created declaratively by either kubectl create --save-config or kubectl apply. The missing annotation will be patched automatically.
pod/redis configured

controlplane ~ ➜  kubectl get pods
NAME            READY   STATUS    RESTARTS      AGE
nginx           1/1     Running   0             34m
newpods-cb427   1/1     Running   2 (39s ago)   34m
newpods-sg99g   1/1     Running   2 (39s ago)   34m
newpods-x4wmv   1/1     Running   2 (39s ago)   34m
redis           1/1     Running   0             2m36s
```


