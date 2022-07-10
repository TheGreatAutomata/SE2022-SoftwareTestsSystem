package com.micro.commonserver.service;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class MinioService {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private int partSize;

    private MinioClient client;

    @SneakyThrows
    public MinioService(String endpoint, String accessKey, String secretKey, int partSize) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.partSize = partSize;
        this.client = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    public void createBucket(String bucketName) throws Exception {
        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 获取全部bucket
     * <p>
     * https://docs.minio.io/cn/java-client-api-reference.html#listBuckets
     */
    public List<Bucket> getAllBuckets() throws Exception {
        return client.listBuckets();
    }

    /**
     * 根据bucketName获取信息
     * @param bucketName bucket名称
     */
    public Optional<Bucket> getBucket(String bucketName) throws Exception {
        return client.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
    }

    /**
     * 根据bucketName删除信息
     * @param bucketName bucket名称
     */
    public void removeBucket(String bucketName) throws Exception {
        client.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    /**
     * 分区上传文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream 文件流
     * @param size 文件大小
     */
    public String putObject(String bucketName, String objectName, InputStream stream, Long size) throws Exception{
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(stream, size, partSize)
                .build();
        ObjectWriteResponse objectWriteResponse = client.putObject(putObjectArgs);
        return objectWriteResponse.object();
    }

    /**
     * 设置bucket的标识
     * @param bucketName
     * @param objectName
     * @param mp
     * @return boolean
     */
    @SneakyThrows
    public boolean setTag(String bucketName, String objectName, Map<String, String>mp)
    {
        client.setObjectTags(
                SetObjectTagsArgs.builder().bucket(bucketName).object(objectName).tags(mp).build());
        return true;
    }

    /**
     * 获取bucket的标识
     * @param bucketName
     * @param objectName
     * @return Map<String, String>
     */
    @SneakyThrows
    public Map<String, String> getTags(String bucketName, String objectName)
    {
        if(hasObject(bucketName, objectName))
        {
            return client.getObjectTags(GetObjectTagsArgs.builder().bucket(bucketName).object(objectName).build()).get();
        }
        else return null;
    }

    /**
     *
     * @param bucketName
     * @deprecated
     * @return boolean
     */
    @SneakyThrows
    public boolean hasBucket(String bucketName)
    {
        return client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    public boolean hasObject(String bucketName, String objectName)
    {
            if(hasBucket(bucketName))
            {
                try {
                    StatObjectResponse objectInfo = getObjectInfo(bucketName, objectName);
                    return objectInfo != null;
                } catch (Exception e) {
                    return false;
                }
            }
            return false;

    }

    /**
     * 获取全部文件
     * @param bucketName
     * @return all objects
     */
    @SneakyThrows
    public Iterable<Result<Item>> listObjects(String bucketName)
    {
        if(!hasBucket(bucketName))
        {
            return null;
        }
        Iterable<Result<Item>> results = client.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).recursive(true).build());
        return results;
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    public List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) throws Exception {
        List<Item> objectList = new ArrayList<>();
        ListObjectsArgs listObjectsArgs = ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .recursive(recursive)
                .build();

        Iterable<Result<Item>> objectsIterator = client
                .listObjects(listObjectsArgs);

        while (objectsIterator.iterator().hasNext()) {
            objectList.add(objectsIterator.iterator().next().get());
        }
        return objectList;
    }

    /**
     * 获取文件外链
     * 这里的 method 方法决定最后链接是什么请求获得
     *  expiry 决定这个链接多久失效
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return url
     */
    public String getObjectURL(String bucketName, String objectName) throws Exception {

        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .method(Method.GET)
                .expiry(7, TimeUnit.DAYS)
                .object(objectName)
                .build();

        return client.getPresignedObjectUrl(args);
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        return client.getObject(getObjectArgs);
    }


    /**
     * 上传文件 base64
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param base64Str 文件base64
     */
//    public String putObject(String bucketName, String objectName, String base64Str) throws Exception{
//        InputStream inputStream = new ByteArrayInputStream(base64Str.getBytes());
//        // 进行解码
//        BASE64Decoder base64Decoder = new BASE64Decoder();
//        byte[] byt = new byte[0];
//        try {
//            byt = base64Decoder.decodeBuffer(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        inputStream = new ByteArrayInputStream(byt);
//        putObject(bucketName, objectName, inputStream, Long.valueOf(byt.length));
//        return objectName;
//    }

    /**
     * 上传文件
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param file 文件
     * @throws Exception
     */
    public String putObject( String bucketName,String objectName, MultipartFile file) throws Exception{
        this.putObject(bucketName, objectName, file.getInputStream(), file.getSize());
        return objectName;
    }

    /**
     * 获取文件信息
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#statObject
     */
    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
        StatObjectArgs statObjectArgs = StatObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        return client.statObject(statObjectArgs);
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     */
    public void removeObject(String bucketName, String objectName) throws Exception {
        RemoveObjectArgs args = RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build();
        client.removeObject(args);

    }

    /**
     * 根据文件名返回对应contentType
     * @param objectName
     * @return
     */
//    private String getContentType(String objectName) {
//        if(FileNameUtil.isPicture(objectName)) {
//            return "image/jpeg";
//        }
//        if(FileNameUtil.isVideo(objectName)) {
//            return "video/mp4";
//        }
//        return null;
//    }

    /**
     * 获取直传链接
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception
     */
    public String presignedPutObject( String bucketName,String objectName) throws Exception{
        GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(bucketName)
                .object(objectName)
                .expiry(7, TimeUnit.DAYS)
                .build();
        return client.getPresignedObjectUrl(getPresignedObjectUrlArgs);
    }

    /**
     * 合并文件
     * @param bucketName
     * @param chunkNames
     * @param targetObjectName
     * @return
     * @throws Exception
     */
    public String composeObject(String bucketName, List<String> chunkNames, String targetObjectName) throws Exception{

        List<ComposeSource> sources = new ArrayList<>(chunkNames.size());
        for (String chunkName : chunkNames) {
            ComposeSource composeSource = ComposeSource.builder()
                    .bucket(bucketName)
                    .object(chunkName)
                    .build();
            sources.add(composeSource);
        }

        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket(bucketName)
                .sources(sources)
                .object(targetObjectName)
                .build();
        ObjectWriteResponse objectWriteResponse = client.composeObject(composeObjectArgs);
        return objectWriteResponse.object();
    }

}

