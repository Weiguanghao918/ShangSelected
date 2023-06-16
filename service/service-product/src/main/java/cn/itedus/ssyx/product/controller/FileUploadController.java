package cn.itedus.ssyx.product.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.product.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 15:20
 * @description: 文件上传接口
 */
@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/admin/product")
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("文件上传")
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) {
        String url = fileUploadService.fileUpload(file);
        return Result.ok(url);
    }
}
