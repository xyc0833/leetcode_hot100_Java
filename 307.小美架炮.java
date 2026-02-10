//学习一下java版本的 小美架炮
import java.util.*;
public class Main{
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);

        //读取炮的数量
        int n = in.nextInt();
        // 2. 分类存储坐标：核心思路和C++一致，只是用List+手动排序替代TreeSet
        // row: key=横坐标x，value=所有x相同的点的纵坐标y列表（比如x=0对应[0,1,2]）
        HashMap<Integer,List<Integer>> row = new HashMap();
        // col: key=纵坐标y，value=所有y相同的点的横坐标x列表（比如y=0对应[0,1,2,3]）
        HashMap<Integer,List<Integer>> col = new HashMap();
        // 存储所有炮的原始坐标，后续遍历用
        List<int[]> nums = new ArrayList();

        //读取每个炮的坐标
        for(int i=0;i<n;i++){
            int x = in.nextInt();
            int y = in.nextInt();
            //用数组存单个炮的坐标
            int []temp = new int[2];
            temp[0] = x;
            temp[1] = y;
            nums.add(temp);

            //===== 填充row：按x分类存y =====
            if(row.containKey(x)){ // 如果x已经在row中，直接添加y
                row.get(x).add(y);
            }else{ // 如果x不在row中，新建列表并添加y
                List<Integer> list = new ArrayList();
                list.add(y);
                row.put(x,list);
            }

            // ===== 填充col：按y分类存x =====
            if(col.containKey(y)){// 如果y已经在col中，直接添加x
                col.get(y).add(x);
            }else{
                List<Integer> list = new ArrayList();
                list.add(x);
                col.put(y,list);
            }
        }
        // 4. 对所有分类列表排序：因为后续要二分查找，必须保证列表有序
        // 对row中每个x对应的y列表排序（比如x=0的y列表[0,2,1]→[0,1,2]）
        //row.entrySet() : map中所有键值对 对象的集合
        for(Map.Entry<Integer,List<Integer>> entry : row.entrySet()){
            Collections.sort(entry.getValue());
        }
        for(Map.Entry<Integer,List<Integer>> entry : col.entrySet()){
            Collections.sort(entry.getValue());
        }

                // 5. 遍历每个炮，计算能攻击的数量
        for(int i = 0 ; i < n ; i++){
            int[] temp = nums.get(i);  // 取出第i个炮的坐标
            int x = temp[0];  // 当前炮的x
            int y = temp[1];  // 当前炮的y
            int cnt = 0;      // 能攻击到的炮的总数，初始为0

            // ===== 第一步：判断上下方向（同x轴的y列表）=====
            List<Integer> list1 = row.get(x);  // 所有和当前炮同x的y列表（已排序）
            // 二分查找y在list1中的索引（比如list1=[0,1,2]，y=0→索引0；y=1→索引1）
            int minIndex1 = function(list1 , y);
            // 上方向能攻击：索引>1 → 说明当前y前面至少有2个元素（炮架+可攻击目标）
            // 比如list1=[0,1,2]，y=2→索引2>1 → 上方向能攻击（炮架是1，攻击0）
            if(minIndex1 > 1) cnt++;
            // 下方向能攻击：索引≤列表长度-3 → 说明当前y后面至少有2个元素
            // 比如list1=[0,1,2]，y=0→索引0 ≤ 3-3=0 → 下方向能攻击（炮架是1，攻击2）
            if(minIndex1 <= list1.size() - 3) cnt++;

            // ===== 第二步：判断左右方向（同y轴的x列表）=====
            List<Integer> list2 = col.get(y);  // 所有和当前炮同y的x列表（已排序）
            // 二分查找x在list2中的索引
            int minIndex2 = function(list2 , x);
            // 左方向能攻击：索引>1 → 前面至少2个元素
            if(minIndex2 > 1) cnt++;
            // 右方向能攻击：索引≤列表长度-3 → 后面至少2个元素
            if(minIndex2 <= list2.size() - 3) cnt++;

            // 输出当前炮能攻击的数量
            System.out.println(cnt);
        }
    }

    //自定义二分查找函数 在有序列表list中 找目标值target的索引，
    //找不到返回-1
    //二分查找 还是参考代码随想录 
    //https://programmercarl.com/0704.%E4%BA%8C%E5%88%86%E6%9F%A5%E6%89%BE.html#%E5%85%B6%E4%BB%96%E8%AF%AD%E8%A8%80%E7%89%88%E6%9C%AC
    public static int function(List<Integer> list,int target){
        int l = 0; //左边界
        int r = list.size();  //右边界
        //左闭右开的区间
        while(l<r){
            int mid = (l+r)/2;
            if(list.get(mid) == target){
                return mid;
            }
            //目标在左半区 调整右边界
            if(list.get(mid)>target){
                r = mid;
            }else{
                l = mid+1;
            }
        }
        //循环结束没找到 返回-1
        return -1;
    }
}
