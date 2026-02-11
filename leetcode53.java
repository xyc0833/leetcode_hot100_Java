//动态规划解法
//1 确定dp数组（dp table）以及下标的含义
//2 确定递推公式
//3 dp数组如何初始化
//4 确定遍历顺序
//5 举例推导dp数组
class Solution {
    public int maxSubArray(int[] nums) {
        //dp[i] 表示「以数组中第 i 个元素结尾的连续子数组的最大和」
        /***
        回到 dp[i] 的定义：以 nums[i] 结尾的最大子数组和。那这个子数组只有两种可能：
        情况 1：把 nums[i] 接在「以 nums[i-1] 结尾的最大子数组」后面，也就是 dp[i-1] + nums[i]；
        情况 2：不接前面的，直接从 nums[i] 重新开始一个子数组，也就是 nums[i] 自己。
         */

        int ans = Integer.MIN_VALUE;
        int [] dp = new int[nums.length];
        //初始化dp[0]
        dp[0] = nums[0];
        ans = dp[0];
        //确定遍历顺序 从左到右
        for(int i = 1;i<nums.length;i++){
            dp[i] = Math.max(dp[i-1]+nums[i],nums[i]);
            ans = Math.max(ans,dp[i]);
        }
        return ans;
    }
}

//贪心的思路：其实挺难想的
//当前“连续和”为负数的时候立刻放弃，从下一个元素重新计算“连续和”，
//因为负数加上下一个元素 “连续和”只会越来越小。
class Solution {
    public int maxSubArray(int[] nums) {
        int result = Integer.MIN_VALUE;
        int count = 0;
        for(int i=0;i<nums.length;i++){
            count = count + nums[i];
            //取区间累计的最大值（相当于不断确定最大子序的终止位置）
            result = Math.max(count,result);
            if(count<=0){
                //重置最大子序起始位置 因为遇到负数一定是拉低总和的
                count = 0;
            }
        }
        return result;
    }
}

//双指针的暴力解法 c++能过  java不行

class Solution {
    public int maxSubArray(int[] nums) {
        int result = nums[0];  // 初始化为第一个元素，防止有负数情况
        for (int i = 0; i < nums.length; i++) {
            int count = 0;
            for (int j = i; j < nums.length; j++) {
                count += nums[j];
                result = Math.max(result, count);
            }
        }
        return result;
    }
}
