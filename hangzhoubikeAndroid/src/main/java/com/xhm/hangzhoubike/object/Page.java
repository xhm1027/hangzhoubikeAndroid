package com.xhm.hangzhoubike.object;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 分页对象
 *
 */
public class Page<E> implements Serializable {

    public static final String  INDEX                 = "query_page_index";
    public static final String  SIZE                  = "query_page_size";

    public static final String  START_ROW             = "startRow";
    public static final String  END_ROW               = "endRow";

    /**
     * 缺省每页的显示记录数为20
     */
    public static final Integer DEFAULT_SIZE_PER_PAGE = 20;

    /**
     * 查询的开始记录数
     */
    private Integer             start;
    /**
     * 查询的结束记录数
     */
    private Integer             end;

    /**
     * 当前的页码
     */
    private Integer             pageIndex             = 1;
    /**
     * 总的页码
     */
    private Integer             pageSize              = 0;

    /**
     * 每页显示的记录数
     */
    private Integer             sizePerpage           = DEFAULT_SIZE_PER_PAGE;

    /**
     * 记录总数
     */
    private Integer             recordCount           = 0;

    /**
     * 数据内容
     */
    private List<E>             datas;

    private boolean             hasNext;

    private boolean             hasPre;

   
    public Page() {
    }

    public Page(Integer pageIndex, int recordCount) {
        this(pageIndex, DEFAULT_SIZE_PER_PAGE, recordCount, new ArrayList<E>());
    }

    /**
     * @param pageIndex
     * @param recordCount
     * @param datas
     */
    public Page(Integer pageIndex, int recordCount, List<E> datas) {
        this(pageIndex, DEFAULT_SIZE_PER_PAGE, recordCount, datas);
    }

    /**
     * @param pageIndex
     * @param sizePerpage
     * @param recordCount
     */
    public Page(Integer pageIndex, Integer sizePerpage, int recordCount) {
        this(pageIndex, sizePerpage, recordCount, new ArrayList<E>());
    }

    /**
     * @param pageIndex
     * @param sizePerpage
     * @param datas
     */
    public Page(Integer pageIndex, Integer sizePerpage, int recordCount, List<E> datas) {
        if (pageIndex != null) {
            this.pageIndex = pageIndex;
        }
        if (sizePerpage != null) {
            this.sizePerpage = sizePerpage;
        }

        this.recordCount = recordCount;

        this.setDatas(datas);

        this.pageSize = this.recordCount / this.sizePerpage;
        if (this.recordCount % this.sizePerpage != 0) {
            this.pageSize++;
        }

        int pre = this.pageIndex - 1;
        this.hasPre = pre > 0;
        int next = this.pageIndex + 1;
        this.hasNext = !(next > this.pageSize);
    }

    /**
     * @return the pageIndex
     */
    public Integer getPageIndex() {
        if (pageIndex == -1) {
            return this.pageSize;
        }
        return pageIndex;
    }

    /**
     * @param pageIndex the pageIndex to set
     */
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * @return the pageSize
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * @return the sizePerpage
     */
    public Integer getSizePerpage() {
        return sizePerpage;
    }

    /**
     * @param sizePerpage the sizePerpage to set
     */
    public void setSizePerpage(Integer sizePerpage) {
        this.sizePerpage = sizePerpage;
    }

    /**
     * @return the datas
     */
    public List<E> getDatas() {
        return datas;
    }

    /**
     * @param datas the datas to set
     */
    public void setDatas(List<E> datas) {
        this.datas = datas;
    }

    /**
     * @return the start
     */
    public Integer getStart() {
        return start;
    }

    /**
     * @return the end
     */
    public Integer getEnd() {
        return end;
    }

    /**
     * @return the recordCount
     */
    public Integer getRecordCount() {
        return recordCount;
    }

    /**
     * @param recordCount the recordCount to set
     */
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * 是否有下一页
     *
     * @return
     */
    public boolean isHasNext() {
        return this.hasNext;
    }

    /**
     * 是否有上一页
     *
     * @return
     */
    public boolean isHasPre() {
        return this.hasPre;
    }


}
