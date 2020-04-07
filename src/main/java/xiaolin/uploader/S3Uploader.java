package xiaolin.uploader;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

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

    @Override
    public String upload(BufferedImage bufferedImage, String path) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", os);
        byte[] buffer = os.toByteArray();
        InputStream is = new ByteArrayInputStream(buffer);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(buffer.length);
        s3.putObject(new PutObjectRequest(bucket, path, is, meta));
        return s3.getUrl(bucket, path).toString();
    }
}
