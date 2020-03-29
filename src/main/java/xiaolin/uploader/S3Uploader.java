package xiaolin.uploader;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class S3Uploader implements Uploader {

    private AmazonS3 s3;
    private String bucket;

    public S3Uploader(String accessKey, String secretKey, String bucket, String region){
        this.bucket = bucket;
        AWSCredentials cred = new BasicAWSCredentials(accessKey, secretKey);
        this.s3 = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(cred)).withRegion(region).build();
    }

    @Override
    public String upload(File file) {
        String path = file.getName();
        s3.putObject(new PutObjectRequest(bucket, path, file).withCannedAcl(CannedAccessControlList.PublicRead));
        return s3.getUrl(bucket, path).toString();
    }
}
