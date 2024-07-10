package com.ideas2it.cms.dto;

public class SuccessMessage<T> {
        private String message;
        private T data;
        private int status;

        public SuccessMessage(String message, T data, int status) {
            this.message = message;
            this.data = data;
            this.status = status;
        }
        public String getMessage() {
            return message;
        }
        public T getData() {
            return data;
        }
        public int getStatus() {
            return status;
        }
}
