//先写代码比较简单的方式 这个
//基于 LinkedHashMap 实现

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ✅ 最近使用 = 移到末尾
    ✅ 淘汰删除 = 删头节点
 * 放入新元素的时候也是 算作一次使用 放在链表的最后面
    LRU 的核心是 “最近最少使用的元素优先淘汰”，关键要满足：
快速查找：用 HashMap 存储键值对，时间复杂度 O (1)；
快速调整顺序：用有序结构（如双向链表）记录元素使用顺序，最近使用的放尾部，最少使用的放头部；
容量限制：达到上限时删除头部（最少使用）元素。
 */


// public class LRUCache {
//     //定义缓存容器： LinkedHashMap 按访问顺序排序
//     private final LinkedHashMap<Integer,Integer> cache;
//     //缓存最大容量
//     private final int capactiy;
//     //构造方法 初始化容量和 linkhashmap
//     public LRUCache(int capactiy){
//         this.capactiy = capactiy;
//         //参数说明
//         //1 initialCapacity 初始容量，设为capacity即可
//         //2 loadFactor 加载因子 默认0.75
//         //（满75%扩容 这里不需要扩容 仅需要淘汰）
//         //3 accessOrder true 按照访问顺序排序 
//         // 最近使用的话是移到末尾   淘汰删除是删头节点
//         //false = 按插入顺序
//         this.cache = new LinkedHashMap<Integer,Integer>(capactiy,0.75f,true){
//             //重写该方法 判断是否删除最老的元素
//             // 实际的删除代码 封装在linkhashmap的源码里面
//             //最近最少使用
//             @Override
//             protected boolean removeEldestEntry(Map.Entry<Integer,Integer> eldest){
//                 //当缓存大小超过容量时 返回ture 自动删除最老的元素
//                 return size()>LRUCache.this.capactiy;
//             }
//         };
//     }
//     //获取数据: key存在返回value 否则返回-1
//     public int get(int key){
//         //LinkedHashMap的get方法会自动把访问的元素移到链表尾部（最近使用）
//         return cache.getOrDefault(key, -1);
//     }
//     //写入数据 key不存在则添加 存在则更新 
//     //容量满时自动删除最少的使用的元素
//     public void put(int key,int value){
//         //put方法会自动把元素移到链表的尾部（最近使用）
//         cache.put(key,value);
//     }

//     // 测试方法（新手可直接运行）
//     public static void main(String[] args) {
//         LRUCache lruCache = new LRUCache(2); // 容量为2
//         lruCache.put(1, 1); // 缓存：{1=1}
//         lruCache.put(2, 2); // 缓存：{1=1, 2=2}
//         System.out.println(lruCache.get(1)); // 访问1，移到尾部 → 输出1，缓存：{2=2, 1=1}
//         lruCache.put(3, 3); // 容量满，删除最少使用的2 → 缓存：{1=1, 3=3}
//         System.out.println(lruCache.get(2)); // 2已被删除 → 输出-1
//         lruCache.put(4, 4); // 容量满，删除最少使用的1 → 缓存：{3=3, 4=4}
//         System.out.println(lruCache.get(1)); // 1已被删除 → 输出-1
//         System.out.println(lruCache.get(3)); // 访问3，移到尾部 → 输出3
//         System.out.println(lruCache.get(4)); // 访问4，移到尾部 → 输出4
//     }

// }


//正规做法

import java.util.HashMap;
import java.util.Map;

// 定义双向链表节点类
class Node {
    // 访问权限设为包私有，方便LRUCache类直接访问
    Node next;
    Node pre;
    int key;
    int value;

    // 节点构造函数：初始化key、value，指针默认null
    public Node(int k, int v) {
        this.key = k;
        this.value = v;
        this.next = null;
        this.pre = null;
    }
}

/**
 * LRU缓存实现类
 * 核心逻辑：HashMap + 双向链表
 * - HashMap：快速查找节点（O(1)）
 * - 双向链表：维护访问顺序（头部=最近使用，尾部=最久未使用）
 */
public class LRUCache {
    // 缓存容量
    private int capacity;
    // 双向链表的虚拟头、虚拟尾节点（简化边界操作）
    private Node head;
    private Node tail;
    // 哈希表：key -> 对应的节点引用
    private Map<Integer, Node> map;

    /**
     * 构造函数：初始化LRU缓存
     * @param capacity 缓存最大容量
     */
    public LRUCache(int capacity) {
        this.capacity = capacity;
        // 初始化虚拟头、尾节点
        this.head = new Node(0, 0);
        this.tail = new Node(0, 0);
        // 初始时双向链表为空，头节点指向尾节点，尾节点指向头节点
        this.head.next = this.tail;
        this.tail.pre = this.head;
        // 初始化哈希表
        this.map = new HashMap<>();
    }

    /**
     * 获取指定key的value
     * @param key 要查找的key
     * @return 存在则返回对应value，不存在返回-1
     */
    public int get(int key) {
        // 1. 检查哈希表中是否存在该key
        if (map.containsKey(key)) {
            Node targetNode = map.get(key);
            // 2. 先从双向链表中移除该节点（因为要更新为最近使用）
            remove(targetNode);
            // 3. 将该节点插入到链表头部（标记为最近使用）
            headInsert(targetNode);
            // 4. 返回节点的value
            return targetNode.value;
        }
        // 5. 不存在该key，返回-1
        return -1;
    }

    /**
     * 插入/更新缓存
     * @param key 要插入的key
     * @param value 要插入的value
     */
    public void put(int key, int value) {
        // 1. 如果key已存在：先删除旧节点
        if (map.containsKey(key)) {
            Node oldNode = map.get(key);
            // 从链表中移除旧节点
            remove(oldNode);
            // 从哈希表中移除旧节点（避免内存泄漏）
            map.remove(oldNode.key);
            // 释放旧节点（Java会自动GC，这里显式置null更直观）
            oldNode = null;
        }

        // 2. 创建新节点
        Node newNode = new Node(key, value);
        // 3. 将新节点插入到链表头部（标记为最近使用）
        headInsert(newNode);
        // 4. 将新节点存入哈希表
        map.put(key, newNode);

        // 5. 检查缓存是否超出容量：超出则删除最久未使用的节点（链表尾部）
        if (map.size() > capacity) {
            // 找到链表尾部的真实节点（虚拟尾节点的前一个节点）
            Node toDelete = tail.pre;
            // 从链表中移除该节点
            remove(toDelete);
            // 从哈希表中移除该节点的key
            map.remove(toDelete.key);
            // 释放节点（Java自动GC）
            toDelete = null;
        }
    }

    /**
     * 从双向链表中移除指定节点
     * @param node 要移除的节点
     */
    private void remove(Node node) {
        // 1. 保存当前节点的前驱、后继节点
        Node preNode = node.pre;
        Node nextNode = node.next;
        // 2. 前驱节点的next指向后继节点（跳过当前节点）
        preNode.next = nextNode;
        // 3. 后继节点的pre指向前驱节点（跳过当前节点）
        nextNode.pre = preNode;
    }

    /**
     * 将节点插入到双向链表的头部（虚拟头节点之后）
     * @param node 要插入的节点
     */
    private void headInsert(Node node) {
        // 1. 保存虚拟头节点的后继节点（原链表第一个节点）
        Node headNext = head.next;
        // 2. 虚拟头节点的next指向当前节点
        head.next = node;
        // 3. 当前节点的pre指向虚拟头节点
        node.pre = head;
        // 4. 当前节点的next指向原链表第一个节点
        node.next = headNext;
        // 5. 原链表第一个节点的pre指向当前节点
        headNext.pre = node;
    }

    // 测试用例（可选）
    public static void main(String[] args) {
        LRUCache lru = new LRUCache(2);
        lru.put(1, 1);
        lru.put(2, 2);
        System.out.println(lru.get(1)); // 输出1，1被标记为最近使用
        lru.put(3, 3); // 容量超2，删除最久未使用的2
        System.out.println(lru.get(2)); // 输出-1，2已被删除
        lru.put(4, 4); // 容量超2，删除最久未使用的1
        System.out.println(lru.get(1)); // 输出-1，1已被删除
        System.out.println(lru.get(3)); // 输出3
        System.out.println(lru.get(4)); // 输出4
    }
}