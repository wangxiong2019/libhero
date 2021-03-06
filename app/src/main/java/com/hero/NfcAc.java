package com.hero;

import android.content.Intent;
import android.widget.TextView;

import com.hero.libhero.nfc.NfcUtil;
import com.hero.libhero.nfc.StringCharByteUtil;
import com.hero.libhero.view.XToast;

import java.io.IOException;

import butterknife.BindView;

/**
 * 创建 by hero
 * 时间 2020/3/26
 * 类名
 */
public class NfcAc extends BaseActivty {
    @BindView(R.id.tv_01)
    TextView tv_01;
    @BindView(R.id.tv_02)
    TextView tv_02;
    @BindView(R.id.tv_03)
    TextView tv_03;

    @BindView(R.id.tv_04)
    TextView tv_04;


    private NfcUtil nfcUtil;

    @Override
    public int getLayout() {
        return R.layout.ac_nfc;
    }


    @Override
    public void initView() {
        nfcUtil = new NfcUtil(mActivity);
        if (nfcUtil.getmNfcAdapter() == null) {
            tv_01.setText("不支持nfc功能");
        }

        StringCharByteUtil.strToHexStr("00103392515AB");
        StringCharByteUtil.hexToStr("30303130333339323531354142");
    }

    /**
     * 遇坑指南
     * 1.鄙人所用的M1卡数据一个块为16字节，卡数据存储的是16进制的byte数组。读取的时候要将16进制byte数组转换为10进制的；写卡的时候要进行转换为16进制的byte数组，而且数据必须为16字节
     * 2.第3块一般不进行数据存储（0、1、2、3块）
     * 3.一般来说第0个扇区的第0块为卡商初始化数据，不能进行写操作
     * 4.要关注Activity的声明周期。onNewIntent中要进行扫描卡片的处理，onResume要禁止前台卡片活动的调度处理， onPause要启用前台卡片活动的调度处理。
     * 5.要修改密钥需要先校验密钥之后修改控制位数据、密钥数据。
     */



    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        String id = nfcUtil.readNFCId(intent);
        tv_01.setText("id=" + id);

        //readM1Block(intent);
        write(intent);
    }

    private void write(Intent intent){
        if (nfcUtil.isMifareClassic(intent)) {
            readTag(intent);

            boolean isTrue = writeM1Block(intent);
            if (isTrue == true) {
                readM1Block(intent);
            }

        } else if (nfcUtil.isMifareUltralight(intent)) {

            if (nfcUtil.clearNTAG4to39(intent)) {
                NTAG(intent);
            }

        }
    }

    String[] strs = {
            "W8ONCC", "WDOT6E", "WCO3NW"
    };
    int num = 0;

    private void NTAG(Intent intent) {


        //NTAG213 读写
        int page = 4;
        String text = strs[num];
        boolean isWriteNTAG = nfcUtil.writeStrNTAG213(intent, page, text);

        if (isWriteNTAG == true) {
            tv_04.setText(nfcUtil.readNTAG213(intent));
            XToast.success(mContext, "num=" + num).show();
            if (num < strs.length - 1) {
                num++;
            }

        }

    }

    private void readTag(Intent intent) {
        String str = nfcUtil.readNFCFromTag(intent);
        tv_03.setText("标签数据：" + str);
    }
    int sectorIndex = 6;//5扇区          下标以0开始
    int blockIndex = 1;//5扇区中的第2块  下标以0开始
    private void readM1Block(Intent intent) {
        try {
            String str = nfcUtil.readM1Block(intent, sectorIndex, blockIndex);

            tv_03.setText(sectorIndex + "扇区,第" + (blockIndex + 1) + "块数据：\n" + str);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    String[] stu = {
            "00101V0HTDETS", "00101IF1D7UVB", "00101DQXK8RXR", "00101BBZYYXOB",
            "00101TFCD1MGW", "001017MOHCAWP", "00101KE4CIYFY", "00101PG1WX85Y",
            "00101JMGOIBVT"};

    String[] jifen = {
            "00101oqwPk7Kg", "00101TVAvulej", "00101pwumja2S",
            "00101du9c7k3T", "00101fARY7x2A", "00101pwumja2S"};

    int num2 = 1;

    //学生id     6扇区1块位置
    //积分卡id   5扇区1块位置  龙霞积分卡id 00103E40A8CA8  测试id 8407EE3A
    private boolean writeM1Block(Intent intent) {


        //String text = "0010194CLPKZX";//00101ZXGBCQJ3
//        String aa="";
//        if(num2<10){
//            aa="0"+num2;
//        }else {
//            aa=num2+"";
//        }

        String text = "123ASD";


        //总位置
        int pos = 4 * sectorIndex + blockIndex;

        try {
            if (nfcUtil.writeM1Block(intent, pos, text)) {
                tv_02.setText("" + "写入成功:" + text);
                num2++;
                return true;
            } else {
                tv_02.setText("" + "写入失败");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        nfcUtil.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        nfcUtil.onResume();
    }


}
