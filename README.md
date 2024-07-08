# minio测试demo

使用了github上的**spring-boot-starter-minio**，亲测可用

上传、下载、删除、获取文件列表，均有demo，可以直接拿去使用

<br/>

# 使用docker容器部署minio

**启动命令：**

```text
docker run --name minio -d -e "MINIO_ACCESS_KEY=minioadmin" -e "MINIO_SECRET_KEY=minioadmin" --publish 9000:9000 --publish 9001:9001 --volume /path/to/minio-persistence:/bitnami/minio/data bitnami/minio:latest
```
