package com.example.binary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tx = (TextView) findViewById(R.id.binary);

        //////////////////////////////////////////////////////
        BinaryTree<String> by=new BinaryTree<>("DataRoot");
        for (int i=0;i<10;i++) {
            by.insertRight("nodeDataRight" + i, by.root);
        }

        by.insertLeft("nodeDataLeft", by.root);
        by.inorder(by.root);

        tx.setText(ss+"\n节点数："+(by.count(by.root)));
        //////////////////////////////////////////////////////
        //////////////////////////////////////////////////////



        //////////////////////////////////////////////////////
    }

    String ss;




    public class BinaryTree<T>{

        public Node<T> root;

        public  BinaryTree() {
            root = new Node<T>();
        }

        public BinaryTree(T x) {
            root = new Node<>(x);
        }

        public boolean insertLeft(T x,Node<T> parent) {
            if(parent==null) return false;
            Node<T> p = new Node<>(x);
            if (parent.lchild == null) {
                parent.lchild = p;
            } else {
                p.lchild = parent.lchild;
                parent.lchild = p;
            }
            return true;
        }

        public boolean insertRight(T x,Node<T> parent) {
            if(parent==null) return false;
            Node<T> p = new Node<>(x);
            if (parent.rchild == null) {
                parent.rchild = p;
            } else {
                p.rchild = parent.rchild;
                parent.rchild = p;
            }
            return true;
        }

        public boolean deleteRight(Node<T> parent) {
            return false;
        }
        public boolean deleteLeft(Node<T> parent) {
            return false;
        }

        //递归求节点总数
        public int count(Node<T> node) {
            if(node==null){
                return 0;
            }
            else{
                int leftTreeNodes=count(node.lchild);
                int rightTreeNodes=count(node.rchild);
                return leftTreeNodes+rightTreeNodes+1;
            }
        }



        public Node<T> search(Node<T> node,T x) {
            return null;
        }

        public boolean insertr(Node<T> a, Node<T> parent) {
            return false;
        }
        public boolean insertl(Node<T> a, Node<T> parent) {
            return false;
        }
        public void inorder(Node<T> node){  //中序遍历
            if (node == null) {
                return;
            } else {
                inorder(node.lchild);
                visit(node.getData());
                inorder(node.rchild);
            }

        }


        public void visit(T x) {
            if(x==null){

            }else {
                if (ss == null) {
                    ss = ("\n" + x);

                    Log.e("binary", ":::::::" + ss);

                } else {
                    ss = ss + ("\n" + x);
                }
            }
        }


    }

    public class Node<T>{

        public Node<T> lchild;
        public T data;
        public Node<T> rchild;




        public Node() {
            data=null;
            lchild = null;
            rchild = null;
        }

        public Node(T x) {
            data=x;
            lchild = null;
            rchild = null;
        }

        public T getData() {

            return data;
        }

        public void setData(T y) {

        }
    }


    public class BinarySort<T>{

        public SortNode<T> root;

        public  BinarySort(int k,T t) {
            root = new SortNode<T>(k,t);
        }

//        public BinarySort(T x) {
//            root = new SortNode(x);
//        }

        public boolean insert(T x,Node<T> parent) {
            if(parent==null) return false;
            Node<T> p = new Node<>(x);
            if (parent.lchild == null) {
                parent.lchild = p;
            } else {
                p.lchild = parent.lchild;
                parent.lchild = p;
            }
            return true;
        }



        public boolean deleteRight(Node<T> parent) {
            return false;
        }
        public boolean deleteLeft(Node<T> parent) {
            return false;
        }

        //递归求节点总数
        public int count(Node<T> node) {
            if(node==null){
                return 0;
            }
            else{
                int leftTreeNodes=count(node.lchild);
                int rightTreeNodes=count(node.rchild);
                return leftTreeNodes+rightTreeNodes+1;
            }
        }



//        public SortNode<T> search(SortNode<T> node,int key) {
//           if(node ==null || key==node.key) return node;
//        }

        public void inorder(Node<T> node){  //中序遍历
            if (node == null) {
                return;
            } else {
                inorder(node.lchild);
                visit(node.getData());
                inorder(node.rchild);
            }

        }


        public void visit(T x) {
            if(x==null){

            }else {
                if (ss == null) {
                    ss = ("\n" + x);

                    Log.e("binary", ":::::::" + ss);

                } else {
                    ss = ss + ("\n" + x);
                }
            }
        }


    }


    public class SortNode<T>{

        int key;
        public SortNode<T> lchild;
        public T data;
        public SortNode<T> rchild;


        public SortNode() {
            key=0;
            data=null;
            lchild = null;
            rchild = null;
        }

        public SortNode(int k,T x) {
            key=k;
            data=x;
            lchild = null;
            rchild = null;
        }

        public T getData() {

            return data;
        }

        public void setData(T y) {

        }
    }





}
