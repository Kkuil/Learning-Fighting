1.��Դ��֮ǰ��Ҫ�˽��һЩ����

Node<K,V>[] table   ��ϣ��ṹ�����������

DEFAULT_INITIAL_CAPACITY��   ����Ĭ�ϳ���16

DEFAULT_LOAD_FACTOR��        Ĭ�ϼ�������0.75



HashMap����ÿһ����������������ݣ�
1.1 �����еļ�ֵ�Զ���
    ������  
			int hash;         //���Ĺ�ϣֵ
            final K key;      //��
            V value;          //ֵ
            Node<K,V> next;   //��һ���ڵ�ĵ�ֵַ
			
			
1.2 ������еļ�ֵ�Զ���
	������
			int hash;         		//���Ĺ�ϣֵ
            final K key;      		//��
            V value;         	 	//ֵ
            TreeNode<K,V> parent;  	//���ڵ�ĵ�ֵַ
			TreeNode<K,V> left;		//���ӽڵ�ĵ�ֵַ
			TreeNode<K,V> right;	//���ӽڵ�ĵ�ֵַ
			boolean red;			//�ڵ����ɫ
					


2.���Ԫ��
HashMap<String,Integer> hm = new HashMap<>();
hm.put("aaa" , 111);
hm.put("bbb" , 222);
hm.put("ccc" , 333);
hm.put("ddd" , 444);
hm.put("eee" , 555);

���Ԫ�ص�ʱ�����ٿ������������
2.1����λ��Ϊnull
2.2����λ�ò�Ϊnull�������ظ������������γ�������ߺ����
2.3����λ�ò�Ϊnull�����ظ���Ԫ�ظ���



//����һ����
//��������ֵ

//����ֵ��������Ԫ�ص�ֵ�����û�и��ǣ�����null
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}


//���ü��������Ӧ�Ĺ�ϣֵ���ٰѹ�ϣֵ����һЩ����Ĵ���
//����⣺����ֵ���Ƿ��ؼ��Ĺ�ϣֵ
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

//����һ�����Ĺ�ϣֵ
//����������
//��������ֵ
//�����ģ�������ظ����Ƿ���
//		   true����ʾ��Ԫ�ص�ֵ���������Ḳ��
//		   false����ʾ��Ԫ�ص�ֵ������������и���
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,boolean evict) {
	    //����һ���ֲ�������������¼��ϣ��������ĵ�ֵַ��
        Node<K,V>[] tab;
		
		//��ʱ�ĵ�����������������¼��ֵ�Զ���ĵ�ֵַ
        Node<K,V> p;
        
		//��ʾ��ǰ����ĳ���
		int n;
		
		//��ʾ����
        int i;
		
		//�ѹ�ϣ��������ĵ�ֵַ����ֵ���ֲ�����tab
		tab = table;

        if (tab == null || (n = tab.length) == 0){
			//1.�����ǰ�ǵ�һ��������ݣ��ײ�ᴴ��һ��Ĭ�ϳ���Ϊ16����������Ϊ0.75������
			//2.������ǵ�һ��������ݣ��ῴ�����е�Ԫ���Ƿ�ﵽ�����ݵ�����
			//���û�дﵽ�����������ײ㲻�����κβ���
			//����ﵽ�������������ײ�����������Ϊԭ�ȵ���������������ȫ��ת�Ƶ��µĹ�ϣ����
			tab = resize();
			//��ʾ�ѵ�ǰ����ĳ��ȸ�ֵ��n
            n = tab.length;
        }

		//��������ĳ��ȸ����Ĺ�ϣֵ���м��㣬�������ǰ��ֵ�Զ�����������Ӧ�����λ��
		i = (n - 1) & hash;//index
		//��ȡ�����ж�ӦԪ�ص�����
		p = tab[i];
		
		
        if (p == null){
			//�ײ�ᴴ��һ����ֵ�Զ���ֱ�ӷŵ����鵱��
            tab[i] = newNode(hash, key, value, null);
        }else {
            Node<K,V> e;
            K k;
			
			//�Ⱥŵ���ߣ������м�ֵ�ԵĹ�ϣֵ
			//�Ⱥŵ��ұߣ���ǰҪ��Ӽ�ֵ�ԵĹ�ϣֵ
			//�������һ������ʱ����false
			//�����һ��������true
			boolean b1 = p.hash == hash;
			
            if (b1 && ((k = p.key) == key || (key != null && key.equals(k)))){
                e = p;
            } else if (p instanceof TreeNode){
				//�ж������л�ȡ�����ļ�ֵ���ǲ��Ǻ�����еĽڵ�
				//����ǣ�����÷���putTreeVal���ѵ�ǰ�Ľڵ㰴�պ�����Ĺ�����ӵ������С�
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
            } else {
				//����������л�ȡ�����ļ�ֵ�Բ��Ǻ�����еĽڵ�
				//��ʾ��ʱ����ҵ�������
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
						//��ʱ�ͻᴴ��һ���µĽڵ㣬���������γ�����
                        p.next = newNode(hash, key, value, null);
						//�жϵ�ǰ�������Ƿ񳬹�8���������8���ͻ���÷���treeifyBin
						//treeifyBin�����ĵײ㻹������ж�
						//�ж�����ĳ����Ƿ���ڵ���64
						//���ͬʱ�����������������ͻ���������ת�ɺ����
                        if (binCount >= TREEIFY_THRESHOLD - 1)
                            treeifyBin(tab, hash);
                        break;
                    }
					//e��			  0x0044  ddd  444
					//Ҫ��ӵ�Ԫ�أ� 0x0055   ddd   555
					//�����ϣֵһ�����ͻ����equals�����Ƚ��ڲ�������ֵ�Ƿ���ͬ
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))){
						 break;
					}

                    p = e;
                }
            }
			
			//���eΪnull����ʾ��ǰ����Ҫ�����κ�Ԫ��
			//���e��Ϊnull����ʾ��ǰ�ļ���һ���ģ�ֵ�ᱻ����
			//e:0x0044  ddd  555
			//Ҫ��ӵ�Ԫ�أ� 0x0055   ddd   555
            if (e != null) {
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null){
					
					//�Ⱥŵ��ұߣ���ǰҪ��ӵ�ֵ
					//�Ⱥŵ���ߣ�0x0044��ֵ
					e.value = value;
				}
                afterNodeAccess(e);
                return oldValue;
            }
        }
		
        //threshold����¼�ľ�������ĳ��� * 0.75����ϣ�������ʱ��  16 * 0.75 = 12
        if (++size > threshold){
			 resize();
		}
        
		//��ʾ��ǰû�и����κ�Ԫ�أ�����null
        return null;
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	