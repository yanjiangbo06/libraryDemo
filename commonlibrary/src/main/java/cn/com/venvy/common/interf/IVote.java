package cn.com.venvy.common.interf;

/**
 * 投票接口
 * IVote<D>更新的所需的数据</D>
 * Created by Arthur on 2017/4/10.
 */

public interface IVote<D> {
    /**
     *添加投票之前的页面
     */
     void addVoteAfterItemView();

    /**
     * 添加投票之后的页面
     */
     void addVoteBeforeItemView();

    /**
     * mqtt更新投票结果
     */
     void updateVoteList(D d);
}
