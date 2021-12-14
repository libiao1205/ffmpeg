package com.example.demo.util.watermark;

import java.util.List;

public class VideoInfo {

    private List<StreamInfo> streams;
    private FormatInfo format;

    public List<StreamInfo> getStreams() {
        return streams;
    }

    public void setStreams(List<StreamInfo> streams) {
        this.streams = streams;
    }

    public FormatInfo getFormat() {
        return format;
    }

    public void setFormat(FormatInfo format) {
        this.format = format;
    }

    public static class StreamInfo {
        private String codec_type;
        private String codec_name;
        private int width;
        private int height;
        private Tags tags;
        private List<Data> side_data_list;

        public String getCodec_type() {
            return codec_type;
        }

        public void setCodec_type(String codec_type) {
            this.codec_type = codec_type;
        }

        public String getCodec_name() {
            return codec_name;
        }

        public void setCodec_name(String codec_name) {
            this.codec_name = codec_name;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public Tags getTags() {
            return tags;
        }

        public void setTags(Tags tags) {
            this.tags = tags;
        }

        public List<Data> getSide_data_list() {
            return side_data_list;
        }

        public void setSide_data_list(List<Data> side_data_list) {
            this.side_data_list = side_data_list;
        }
    }

    public static class FormatInfo {

        private String duration;
        private String bit_rate;

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getBit_rate() {
            return bit_rate;
        }

        public void setBit_rate(String bit_rate) {
            this.bit_rate = bit_rate;
        }
    }

    public static class Tags {

        private String rotate;

        public String getRotate() {
            return rotate;
        }

        public void setRotate(String rotate) {
            this.rotate = rotate;
        }
    }

    public static class Data {
        private int rotation;

        public int getRotation() {
            return rotation;
        }

        public void setRotation(int rotation) {
            this.rotation = rotation;
        }
    }
}
