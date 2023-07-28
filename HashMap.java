final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
		// 初始化一个用来存放当前HashMap的临时变量
		// 为了避免后续操作多次从内存中取map而提高性能损耗
        Node<K,V>[] tab;
		// 也是一个中转变量，用来存放当前下标占用元素的地址值，后续会进行更详细的说明
		Node<K,V> p;
		// 初始化两个存放当前HashMap的长度和当前下标的变量
		int n, i;


		// 这里就是我上面所叙述的临时存放当前HashMap
		tab = table
		// 将当前HashMap的长度赋值给变量n，便于后续的使用
		n = tab.length
		// 判断当前HashMap是否为空或者HashMap的长度是否为空
        if (tab == null || n == 0) {
			// 扩容操作
			tab = resize()
            n = tab.length;
        }
		// 计算当前元素应该存入的位置
		i = (n - 1) & hash
		p = tab[i]
		// 如果存入位置没有元素，则直接赋值即可
        if (p == null)
            tab[i] = newNode(hash, key, value, null);
        else {
			// 如果有元素
            Node<K,V> e; 
			K k;
			// 判断当前存储的元素的hash值是否相等
			// 且key的引用是否相等或key值相等
			// 注意：b2和b3的判断是为了避免哈希碰撞的情况发生，
			// 例如：abc和acD的计算哈希值是一样的，这就出现了哈希碰撞
			// 本来abc这个key和acD这个key是不冲突，但是通过计算得出冲突了，我们就可以通过二次比较key值是否相等的方式进行解决
			k = p.key
			Boolean b1 = p.hash == hash
			Boolean b2 = k == key
			Boolean b3 = key != null && key.equals(k)
            if (b1 && (b2 || b3)) { 
				// 如果出现了hash值一样且key值一样，保存当前索引值
				e = p;
            }
			// 如果没出现，判断当前元素是否是一个红黑树
            else if (p instanceof TreeNode)
				// 如果是则进行红黑树操作
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
				// 如果没有出现上面的情况，则按链表处理
				// 循环遍历链表
                for (int binCount = 0; ; ++binCount) {
					// 将下一个值赋值给临时变量
					e = p.next
					// 如果下一个值为空，则直接接在这后面
                    if (e == null) {
                        p.next = newNode(hash, key, value, null);
						// 判断当前链表有没有达到红黑树化的门槛
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
							// 如果达到了，就进行红黑树化，然后瑞出循环
                            treeifyBin(tab, hash);
                        break;
                    }
					// ①  与上述同理
					k = p.key
					Boolean b4 = e.hash == hash
					Boolean b5 = k == key
					Boolean b6 = key != null && key.equals(k)
                    if (b3 && (b4 || b5){
						break;
                    }
                    p = e;
                }
            }
			// 这一个if判断其实可以接到①所在代码块中，其实就是在当发生了key值一样的时候，需不需要进行覆盖操作
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
				// 这里的onlyIfAbsent，就是我们在putVal中传递的参数
				// 当发生了key相等  且  onlyIfAbsent为false或当前存储的值为空时，进行覆盖操作
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
				// 然后返回覆盖前的值
                return oldValue;
            }
        }
		// 判断当前容量是否达到了扩容阈值
        if (++size > threshold)
            resize();
		// 没有值被覆盖，则返回null
        return null;
    }



	// -------------------------------------------------------------------------------------------------

final Node<K,V>[] resize() {
		// 这一步的操作也是为了减少性能损耗，在局部变量里存一份，方便后续使用
        Node<K,V>[] oldTab = table;
		// 判断当前HashMap是否为空，换句话说就是判断是否是第一次添加元素
		// 如果是，则将当前容量设置为0
		// 否则就设置为当前HashMap的长度
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
		// 这一步和第一步是一样的意思，存一份局部变量，以便后续使用
        int oldThr = threshold;
		// 初始化新容量（newCap）和新的扩容阈值（newThr）
        int newCap, newThr = 0;
		// 判断当前的HashMap的容量是否为0
        if (oldCap > 0) {
			// 如果不为0，则判断当前HashMap的容量是否已经大于HashMap最大可存储容量
			// 如果是，首先将当前扩容阈值设置为最大可用整数
			// 然后直接返回当前HashMap
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
			// 首先将现在的HashMap容量赋值给新容量变量
			// 然后再将新容量进行左移一位（乘2），判断其是否大于HashMap的最大存储上限
			// 如果不超过，再进行判断当前的HashMap容量是否大于等于当前的初始化容量
			else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY) {
						// 如果两个都满足，则进行扩容为原来的两倍
						newThr = oldThr << 1; // double threshold
                     }
                
        }
		// 判断当前的扩容阈值是否大于0,
        else if (oldThr > 0) // initial capacity was placed in threshold
			// 如果是则将当前的HashMap容量设置为当前的扩容阈值
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
			// 如果当前HashMap的长度不仅为空，且扩容阈值也为空的话，直接初始化
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
		// 判断新阈值是否为零
		// 第一次来肯定为0
        if (newThr == 0) {
			// 如果为0，则将新容量乘以加载因子赋值给ft
			// 然后判断ft是否小于HashMap的最大存储上限。如果小于，则将ft转换为整数作为新扩容阈值。如果大于等于最大存储上限，则将新扩容阈值设置为最大可用整数。
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
		// 将计算好的新阈值赋值给当前HashMap的阈值
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
		// 创建一个新的数组节点
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
		// 将新创建的数组节点赋值给当前HashMap，开始扩容操作
        table = newTab;
		// 判断当前的（老的）HashMap是否为空，为空直接返回newTab
        if (oldTab != null) {
			// 不为空，开始遍历当前HashMap,进行扩容
            for (int j = 0; j < oldCap; ++j) {、
				// 初始化一个中间元素
                Node<K,V> e;
				e = oldTab[j]
				// 判断当前元素是否为空
                if (e != null) {
					// 不为空，首先将当前元素设置为空
                    oldTab[j] = null;
					// 判断当前元素下方是否有其他元素
                    if (e.next == null)
						// 如果没有，则通过当前新HashMap的容量和当前元素的索引进行计算新的索引位置
						// 然后直接将当前元素赋值给新位置
                        newTab[e.hash & (newCap - 1)] = e;
					// 判断当前元素下方的元素是否是红黑树节点
                    else if (e instanceof TreeNode)
						// 如果节点是红黑树节点，则调用split方法将红黑树节点分割为两个子树并放入新的哈希表中。
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order、
						// 如果节点是链表节点，则根据节点的哈希值重新分配到新的哈希表中的对应位置。
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
							// 接着，通过节点的哈希值与旧容量进行按位与操作，判断该节点应该放入低位链表还是高位链表。
							// 如果结果为0，则表示应该放入低位链表
                            if ((e.hash & oldCap) == 0) {
								// 判断当前低位链表的尾部是否为空
								// 为空，则直接进行赋值
                                if (loTail == null)
                                    loHead = e;
								// 不为空则放在链表尾部
                                else
                                    loTail.next = e;
								// 最后将当前元素作为尾节点
                                loTail = e;
                            }
							// 否则，则放入高位链表
							// 与上同
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
						// 有值进行进行赋值操作
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
						// 有值进行进行赋值操作
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
		// 返回扩容后的HashMap
        return newTab;
    }