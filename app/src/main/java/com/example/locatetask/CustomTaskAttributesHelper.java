package com.example.locatetask;

public class CustomTaskAttributesHelper {
        private int TASK_ID;
        private String TASK_NAME;
        private String TASK_DATE;
        private String TASK_TIME;
        private String TASK_LOCATION;


         CustomTaskAttributesHelper(int TASK_ID, String TASK_NAME, String TASK_DATE, String TASK_TIME,String TASK_LOCATION) {
            this.TASK_ID = TASK_ID;
            this.TASK_NAME = TASK_NAME;
            this.TASK_DATE = TASK_DATE;
            this.TASK_TIME = TASK_TIME;
            this.TASK_LOCATION = TASK_LOCATION;
        }

         int getTASK_ID() {
            return TASK_ID;
        }

         void setTASK_ID(int TASK_ID) {
            this.TASK_ID = TASK_ID;
        }

         String getTASK_NAME() {
            return TASK_NAME;
        }

         void setTASK_NAME(String TASK_NAME) {
            this.TASK_NAME = TASK_NAME;
        }

         String getTASK_DATE() {
            return TASK_DATE;
        }

         void setTASK_DATE(String TASK_DATE) {
            this.TASK_DATE = TASK_DATE;
        }

         String getTASK_TIME() {
            return TASK_TIME;
        }

         void setTASK_TIME(String TASK_TIME) {
            this.TASK_TIME = TASK_TIME;
        }

         void  setTASK_LOCATION(String TASK_LOCATION)
        {
            this.TASK_LOCATION = TASK_LOCATION;
        }

         String getTASK_LOCATION()
        {
            return this.TASK_LOCATION;
        }
    }


