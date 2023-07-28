final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
		// ��ʼ��һ��������ŵ�ǰHashMap����ʱ����
		// Ϊ�˱������������δ��ڴ���ȡmap������������
        Node<K,V>[] tab;
		// Ҳ��һ����ת������������ŵ�ǰ�±�ռ��Ԫ�صĵ�ֵַ����������и���ϸ��˵��
		Node<K,V> p;
		// ��ʼ��������ŵ�ǰHashMap�ĳ��Ⱥ͵�ǰ�±�ı���
		int n, i;


		// �����������������������ʱ��ŵ�ǰHashMap
		tab = table
		// ����ǰHashMap�ĳ��ȸ�ֵ������n�����ں�����ʹ��
		n = tab.length
		// �жϵ�ǰHashMap�Ƿ�Ϊ�ջ���HashMap�ĳ����Ƿ�Ϊ��
        if (tab == null || n == 0) {
			// ���ݲ���
			tab = resize()
            n = tab.length;
        }
		// ���㵱ǰԪ��Ӧ�ô����λ��
		i = (n - 1) & hash
		p = tab[i]
		// �������λ��û��Ԫ�أ���ֱ�Ӹ�ֵ����
        if (p == null)
            tab[i] = newNode(hash, key, value, null);
        else {
			// �����Ԫ��
            Node<K,V> e; 
			K k;
			// �жϵ�ǰ�洢��Ԫ�ص�hashֵ�Ƿ����
			// ��key�������Ƿ���Ȼ�keyֵ���
			// ע�⣺b2��b3���ж���Ϊ�˱����ϣ��ײ�����������
			// ���磺abc��acD�ļ����ϣֵ��һ���ģ���ͳ����˹�ϣ��ײ
			// ����abc���key��acD���key�ǲ���ͻ������ͨ������ó���ͻ�ˣ����ǾͿ���ͨ�����αȽ�keyֵ�Ƿ���ȵķ�ʽ���н��
			k = p.key
			Boolean b1 = p.hash == hash
			Boolean b2 = k == key
			Boolean b3 = key != null && key.equals(k)
            if (b1 && (b2 || b3)) { 
				// ���������hashֵһ����keyֵһ�������浱ǰ����ֵ
				e = p;
            }
			// ���û���֣��жϵ�ǰԪ���Ƿ���һ�������
            else if (p instanceof TreeNode)
				// ���������к��������
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            else {
				// ���û�г���������������������
				// ѭ����������
                for (int binCount = 0; ; ++binCount) {
					// ����һ��ֵ��ֵ����ʱ����
					e = p.next
					// �����һ��ֵΪ�գ���ֱ�ӽ��������
                    if (e == null) {
                        p.next = newNode(hash, key, value, null);
						// �жϵ�ǰ������û�дﵽ����������ż�
                        if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
							// ����ﵽ�ˣ��ͽ��к��������Ȼ�����ѭ��
                            treeifyBin(tab, hash);
                        break;
                    }
					// ��  ������ͬ��
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
			// ��һ��if�ж���ʵ���Խӵ������ڴ�����У���ʵ�����ڵ�������keyֵһ����ʱ���費��Ҫ���и��ǲ���
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
				// �����onlyIfAbsent������������putVal�д��ݵĲ���
				// ��������key���  ��  onlyIfAbsentΪfalse��ǰ�洢��ֵΪ��ʱ�����и��ǲ���
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
				// Ȼ�󷵻ظ���ǰ��ֵ
                return oldValue;
            }
        }
		// �жϵ�ǰ�����Ƿ�ﵽ��������ֵ
        if (++size > threshold)
            resize();
		// û��ֵ�����ǣ��򷵻�null
        return null;
    }



	// -------------------------------------------------------------------------------------------------

final Node<K,V>[] resize() {
		// ��һ���Ĳ���Ҳ��Ϊ�˼���������ģ��ھֲ��������һ�ݣ��������ʹ��
        Node<K,V>[] oldTab = table;
		// �жϵ�ǰHashMap�Ƿ�Ϊ�գ����仰˵�����ж��Ƿ��ǵ�һ�����Ԫ��
		// ����ǣ��򽫵�ǰ��������Ϊ0
		// ���������Ϊ��ǰHashMap�ĳ���
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
		// ��һ���͵�һ����һ������˼����һ�ݾֲ��������Ա����ʹ��
        int oldThr = threshold;
		// ��ʼ����������newCap�����µ�������ֵ��newThr��
        int newCap, newThr = 0;
		// �жϵ�ǰ��HashMap�������Ƿ�Ϊ0
        if (oldCap > 0) {
			// �����Ϊ0�����жϵ�ǰHashMap�������Ƿ��Ѿ�����HashMap���ɴ洢����
			// ����ǣ����Ƚ���ǰ������ֵ����Ϊ����������
			// Ȼ��ֱ�ӷ��ص�ǰHashMap
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
			// ���Ƚ����ڵ�HashMap������ֵ������������
			// Ȼ���ٽ���������������һλ����2�����ж����Ƿ����HashMap�����洢����
			// ������������ٽ����жϵ�ǰ��HashMap�����Ƿ���ڵ��ڵ�ǰ�ĳ�ʼ������
			else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                     oldCap >= DEFAULT_INITIAL_CAPACITY) {
						// ������������㣬���������Ϊԭ��������
						newThr = oldThr << 1; // double threshold
                     }
                
        }
		// �жϵ�ǰ��������ֵ�Ƿ����0,
        else if (oldThr > 0) // initial capacity was placed in threshold
			// ������򽫵�ǰ��HashMap��������Ϊ��ǰ��������ֵ
            newCap = oldThr;
        else {               // zero initial threshold signifies using defaults
			// �����ǰHashMap�ĳ��Ȳ���Ϊ�գ���������ֵҲΪ�յĻ���ֱ�ӳ�ʼ��
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
		// �ж�����ֵ�Ƿ�Ϊ��
		// ��һ�����϶�Ϊ0
        if (newThr == 0) {
			// ���Ϊ0�������������Լ������Ӹ�ֵ��ft
			// Ȼ���ж�ft�Ƿ�С��HashMap�����洢���ޡ����С�ڣ���ftת��Ϊ������Ϊ��������ֵ��������ڵ������洢���ޣ�����������ֵ����Ϊ������������
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
		// ������õ�����ֵ��ֵ����ǰHashMap����ֵ
        threshold = newThr;
        @SuppressWarnings({"rawtypes","unchecked"})
		// ����һ���µ�����ڵ�
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
		// ���´���������ڵ㸳ֵ����ǰHashMap����ʼ���ݲ���
        table = newTab;
		// �жϵ�ǰ�ģ��ϵģ�HashMap�Ƿ�Ϊ�գ�Ϊ��ֱ�ӷ���newTab
        if (oldTab != null) {
			// ��Ϊ�գ���ʼ������ǰHashMap,��������
            for (int j = 0; j < oldCap; ++j) {��
				// ��ʼ��һ���м�Ԫ��
                Node<K,V> e;
				e = oldTab[j]
				// �жϵ�ǰԪ���Ƿ�Ϊ��
                if (e != null) {
					// ��Ϊ�գ����Ƚ���ǰԪ������Ϊ��
                    oldTab[j] = null;
					// �жϵ�ǰԪ���·��Ƿ�������Ԫ��
                    if (e.next == null)
						// ���û�У���ͨ����ǰ��HashMap�������͵�ǰԪ�ص��������м����µ�����λ��
						// Ȼ��ֱ�ӽ���ǰԪ�ظ�ֵ����λ��
                        newTab[e.hash & (newCap - 1)] = e;
					// �жϵ�ǰԪ���·���Ԫ���Ƿ��Ǻ�����ڵ�
                    else if (e instanceof TreeNode)
						// ����ڵ��Ǻ�����ڵ㣬�����split������������ڵ�ָ�Ϊ���������������µĹ�ϣ���С�
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order��
						// ����ڵ�������ڵ㣬����ݽڵ�Ĺ�ϣֵ���·��䵽�µĹ�ϣ���еĶ�Ӧλ�á�
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
							// ���ţ�ͨ���ڵ�Ĺ�ϣֵ����������а�λ��������жϸýڵ�Ӧ�÷����λ�����Ǹ�λ����
							// ������Ϊ0�����ʾӦ�÷����λ����
                            if ((e.hash & oldCap) == 0) {
								// �жϵ�ǰ��λ�����β���Ƿ�Ϊ��
								// Ϊ�գ���ֱ�ӽ��и�ֵ
                                if (loTail == null)
                                    loHead = e;
								// ��Ϊ�����������β��
                                else
                                    loTail.next = e;
								// ��󽫵�ǰԪ����Ϊβ�ڵ�
                                loTail = e;
                            }
							// ����������λ����
							// ����ͬ
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
						// ��ֵ���н��и�ֵ����
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
						// ��ֵ���н��и�ֵ����
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
		// �������ݺ��HashMap
        return newTab;
    }