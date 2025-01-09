package com.example.danjamserver.util.exception;

public class FileNotFoundException extends BaseException{
  public FileNotFoundException() {
    super(ResultCode.FILE_UPLOAD_FAILED);
  }

  public FileNotFoundException(String detailMessage) {
    super(ResultCode.FILE_UPLOAD_FAILED, detailMessage);
  }
}
