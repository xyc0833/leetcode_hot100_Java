//自己的暴力解法
class Solution {
    public int[] twoSum(int[] nums, int target) {
        int [] res = new int[2];
        int n = nums.length;
        //System.out.println(n);
        for(int i=0;i<nums.length;i++){
            for(int j=i+1;j<n;j++){
                if((nums[i]+nums[j]) == target )
                    return new int []{i,j};
            }
        }
        return res;
    }
}