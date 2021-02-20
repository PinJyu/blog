package cn.nhmt.blog.ao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Description: TODO
 * @Date: 2020-04-20 00:56
 * @Author: PinJyu
 * @Version: 1.0
 **/
public class SearchResult<T> {

    private List<T> result;
    private String key;
    private boolean hasPage;
    private int currentPage;
    private int totalPageCount;

    public boolean isSuccess() {
        return result != null && result.size() != 0;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isHasPage() {
        return hasPage;
    }

    public void setHasPage(boolean hasPage) {
        this.hasPage = hasPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "result=" + result +
                ", key='" + key + '\'' +
                ", hasPage=" + hasPage +
                ", currentPage=" + currentPage +
                ", totalPageCount=" + totalPageCount +
                '}';
    }
}
