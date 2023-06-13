package cn.itedus.ssyx.product.service.impl;

import cn.itedus.ssyx.common.utils.DesUtils;
import cn.itedus.ssyx.product.service.FileUploadService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 15:22
 * @description: 文件上传服务接口实现类
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Value("${aliyun.endpoint}")
    private String endPoint;
    @Value("${aliyun.keyid}")
    private String accessKey;
    @Value("${aliyun.keysecret}")
    private String secretKey;
    @Value("${aliyun.bucketname}")
    private String bucketName;


    @Override
    public String fileUpload(MultipartFile file) {
        try {
            accessKey = DesUtils.decrypt(accessKey, DesUtils.Mode.DES, "abcdefgh");
            secretKey = DesUtils.decrypt(secretKey, DesUtils.Mode.DES, "abcdefgh");
            //创建ossClient实例
            OSS ossClient = new OSSClientBuilder().build(endPoint, accessKey, secretKey);
            //上传文件流
            InputStream inputStream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            //生成随机唯一值，使用uuid，添加到文件名称里面
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + fileName;
            //按照当前日期，创建文件夹，上传到创建文件夹里面去
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            fileName = timeUrl + "/" + fileName;
            //调用方法实现上传
            ossClient.putObject(bucketName, fileName, inputStream);
            //关闭ossClient实例
            ossClient.shutdown();
            //上传之后返回文件路径
            String url = "https://" + bucketName + "." + endPoint + "/" + fileName;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
