package process;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class Document implements FileCode {
	public static String contentEndCheck(BufferedImage img, int[] row) {
		int lineNum = 0;
		ArrayList<Integer> seperator = new ArrayList<>();

		int h = img.getHeight();
		int w = img.getWidth();

		int lineH = h / 170;
		int up = 0;
		int down = 0;
		for (int y2 = h - 1; y2 > 0; y2--) {
			up = 0;
			down = 0;
			for (int i = y2; i > 0; i--) {
				if (row[i] > 10) {
					y2 = i - 1;
					down = i + 5 < h - 1 ? i + 5 : h - 1;
					break;
				}
				if (i == 0) {
					y2 = 0;
				}
			}
			for (int i = y2; i > 0; i--) {
				if (row[i] < 13) {
					y2 = i;
					up = i - 5 > 0 ? i - 5 : 0;
					break;
				}
				if (i == 0) {
					y2 = 0;
				}
			}
			if ((down - up > lineH) && (down - up < h * 0.5D)) {
				seperator.add(Integer.valueOf(up));
				seperator.add(Integer.valueOf(down));
				lineNum++;
				BufferedImage lineImg = img.getSubimage(0, up, w, down - up);
				File imgFile2 = new File(System.currentTimeMillis() + ".png");
				try {
					ImageIO.write(lineImg, "png", imgFile2);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String txt = OCR.recognizeText(imgFile2, "-psm 7");
				if (txt.isEmpty()) {
					txt = OCR.recognizeText(imgFile2, "");
				}
				System.out.println("12_" + txt);
				imgFile2.delete();

				if (txt.matches(".*为其诉讼代理.{1,3}|.*代理诉讼.{0,3}|.{0,4}授权委[托抚拄]书.{0,5}|.*委托.{0,4}|.{0,5}法律服务.{0,5}")) {
					return "5";
				}
				if (txt.matches(".*撤回.{0,3}执行.{0,4}")) {
					return "30";
				}
			}
			if (lineNum > 5) {
				return "";
			}
		}
		return "";
	}

	public static boolean fileEndCheck(BufferedImage img, int[] row, double threshold) {
		int h = img.getHeight();
		int blankLen = 0;
		for (int y = h - 1; y > 0; y--) {
			if ((row[y] > 20) && (row[(y - 1)] > 20)) {
				break;
			}
			blankLen++;
		}
		if (blankLen > h * threshold) {
			for (int y = 0; y < h; y++) {
				int up = 0;
				int down = 0;
				for (int i = y; i < h; i++) {
					if (row[i] > 15) {
						up = i;
						y = i;
						break;
					}
					if (i == h - 1) {
						y = h;
					}
				}
				for (int i = y; i < h; i++) {
					if (row[i] < 15) {
						down = i;

						if (down - up > h * 0.1D) {
							return false;
						}
						y = i;
						break;
					}
				}
			}
			System.out.println("file blank end");
			return true;
		}
		return false;
	}

	public static boolean endCheck(BufferedImage img, int[] row) {
		int lineNum = 0;
		ArrayList<Integer> seperator = new ArrayList<>();

		int h = img.getHeight();
		int w = img.getWidth();

		int lineH = h / 100;
		int up = 0;
		int down = 0;
		for (int y2 = h - 1; y2 > 0; y2--) {
			up = 0;
			down = 0;
			for (int i = y2; i > 0; i--) {
				if (row[i] > 10) {
					y2 = i - 1;
					down = i + 5 < h - 1 ? i + 5 : h - 1;
					break;
				}
				if (i == 0) {
					y2 = 0;
				}
			}
			for (int i = y2; i > 0; i--) {
				if (row[i] < 13) {
					y2 = i;
					up = i - 5 > 0 ? i - 5 : 0;
					break;
				}
				if (i == 0) {
					y2 = 0;
				}
			}
			if ((down - up > lineH) && (down - up < h * 0.5D)) {
				seperator.add(Integer.valueOf(up));
				seperator.add(Integer.valueOf(down));
				lineNum++;
				BufferedImage lineImg = img.getSubimage(0, up, w, down - up);
				File imgFile2 = new File(System.currentTimeMillis() + ".png");
				try {
					ImageIO.write(lineImg, "png", imgFile2);
				} catch (IOException e) {
					e.printStackTrace();
				}
				String txt = OCR.recognizeText(imgFile2, "-psm 7");
				if (txt.isEmpty()) {
					txt = OCR.recognizeText(imgFile2, "");
				}
				System.out.println("12_" + txt);
				imgFile2.delete();

				if (txt.matches(".{0,2}[此北]致.{0,5}|.{0,2}具状.*|.{0,2}原告人.*|.{0,2}申请.{2,6}|.{0,2}答[辨辩].{2,6}"
						+ "|.{1,8}[耳年].*[月目用门].*[口日曰白门].{0,3}|.{3,10}[人八]民法[阮院].{0,2}|.{0,1}附.{0,1}"
						+ "|.*起诉人:.*|.{1,6}人:.{2,4}|[此北].|.{0,2}[上公]诉[人八]:.{1,4}|[申巾]请[人八]：.{0,3}"
						+ "|法定代表.{0,3}[(]印[章童][)]|.*法律服务所|.*律[师帅]事务所|.{1,10}律师|.?[窜审]判[长员].{1,5}"
						+ "|.{2,8}局|负责人:.*|[辨辩]护[人儿].{2,4}|年月[日曰门]|谢谢|检察员:.*|.{2,6}[市巾]级.{0,3}法院.{0,4}")) {
					System.out.println("file end feature");
					return true;
				}
			}
			if (lineNum > 4) {
				return false;
			}
		}
		return false;
	}

	public static String check(BufferedImage img) {
		int h = img.getHeight();
		int w = img.getWidth();
		img = img.getSubimage((int) (w * 0.1D), (int) (h * 0.05D), (int) (w * 0.8D), (int) (h * 0.86D));
		int lineH = h / 85;
		byte[][] pixels = Utils.binarition(img, 750);
		img = Utils.rectify(pixels);
		pixels = Utils.binarition(img, 750);
		pixels = Utils.repair(pixels);
		img = Utils.pixels2Image(pixels);

		h = img.getHeight();
		w = img.getWidth();
		ArrayList<Integer> seperator = new ArrayList<>();
		int[] row = new int[h];
		row = Utils.Tohisto(img, "row");
		BufferedImage lineImg = null;
		String txt = null;
		int lineNum = 0;
		for (int y = 0; y < h; y++) {
			int up = 0;
			int down = 0;
			for (int i = y; i < h; i++) {
				if (row[i] > 10) {
					y = i + 1;
					up = i - 5 > 0 ? i - 5 : 0;
					break;
				}
				if (i == h - 1) {
					y = h;
				}
			}
			for (int i = y; i < h; i++) {
				if (row[i] < 13) {
					y = i;
					down = i + 5 > h - 1 ? h - 1 : i + 5;
					break;
				}
				if (i == h - 1) {
					y = h;
				}
			}
			if ((down - up > lineH) && (down - up < h * 0.35D)) {
				seperator.add(Integer.valueOf(up));
				seperator.add(Integer.valueOf(down));
				lineImg = img.getSubimage(0, up, w, down - up);
				if (lineImg == null) {
					continue;
				}
				lineNum++;
				File imgFile = new File(System.currentTimeMillis() + ".png");
				try {
					ImageIO.write(lineImg, "png", imgFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				txt = OCR.recognizeText(imgFile, "-psm 7");
				if ((txt.isEmpty()) || (txt.matches("\\w"))) {
					txt = OCR.recognizeText(imgFile, "");
				}
				System.out.println("1_" + txt);
				imgFile.delete();
				if (txt.matches(".{0,4}诉讼文书卷.{0,4}")) {
					return fileCover;
				}
				if (txt.matches("卷内文书目录")) {
					return index;
				}
				if (txt.matches("受案登记表")) {
					return receiveRegiForm;
				}
				if (txt.matches(".{0,3}案件移送起诉告知书.?")) {
					return transferCaseNotification;
				}
				if (txt.matches(".{0,3}立案决定书.?")) {
					return fileCaseDecision;
				}
				if (txt.matches(".{0,3}传唤证.?|.{0,3}拘传证.?")) {
					return summonNotification;
				}
				if (txt.matches(".{0,3}搜查证.?")) {
					return searchWarrant;
				}
				if (txt.matches(".{0,3}扣押物品、文件清单.?")) {
					return distrainList;
				}
				if (txt.matches(".{0,3}拘留证.?|.{0,3}拘留通知书.?")) {
					return detentionNotification;
				}
				if (txt.matches("提请批准逮捕书")) {
					return arrestApplication;
				}
				if (txt.matches(".{0,3}不批准逮捕决定书.?|.{0,3}批准逮捕理由说明书.?|.{0,3}逮捕证.{0,3}")) {
					return arrestNotification;
				}
				if (txt.matches(".{0,3}变更.?押期限通知书.{0,3}")) {
					return lengthenInveNotification;
				}
				if (txt.matches(".{0,3}释放通知书.{0,3}")) {
					return releaseNotification;
				}
				if (txt.matches(".{0,3}取保候审.{0,5}书.{0,3}")) {
					return bailPendingTrial;
				}
				if (txt.matches(".?起诉意见书.?")) {
					return prosecutionProposal;
				}
				if (txt.matches(".?征收社会抚养费决定书.?|执行和.?协议|.{0,3}鉴定意见通知书.{0,3}|.{0,3}鉴定聘请书.{0,3}")) {
					return other;
				}
				if (txt.matches(".{0,5}诉讼证据卷.{0,3}")) {
					return evidenceCover;
				}
				if (txt.matches(".{0,5}嫌疑人.{0,3}信息查询登记表")) {
					return suspectBasicInfo;
				}
				if (txt.matches(".{0,3}违法犯罪嫌疑人.{0,3}到案过程")) {
					return suspectProcedureIllustration;
				}
				if (txt.matches(".{0,3}犯罪嫌疑人诉讼权利义务告知书.{0,3}")) {
					return suspectLawsuitRightNotificationF;
				}
				if (txt.matches(".{0,5}问笔录.{0,3}")) {
					return interrogationRecordF;
				}
				if (txt.matches(".{0,3}证人诉讼权利义务告知书.{0,3}")) {
					return witnessLawsuitRightNotification;
				}
				if (txt.matches(".{0,3}辨认笔录.{0,3}")) {
					return inquiryRecordFront;
				}
				if (txt.matches(".{0,3}[搜艘]查笔录.{0,3}")) {
					return inquiryRecordFront;
				}
				if (txt.matches("鉴定资质证明|鉴定意.?书")){
					return expertConclusion;
				}
				if (txt.matches(".{0,5}鉴定结论书.{0,3}|物证鉴定书|鉴定资质证明")) {
					if ((fileEndCheck(img, row, 0.25D)) || (endCheck(img, row))) {
						return expertConclusion;
					}
					return expertConclusionFront;
				}
				if (txt.matches(".{0,3}籍证明.{0,3}")){
					return identification;
				}
				if (txt.matches(".{0,3}现场搜查照片.{0,3}|[玉王]见场指认照片|现场指认照片")){
					return onTheSpotSVRecord;
				}
			}
			if (lineNum > 3) {
				break;
			}

		}
		if (fileEndCheck(img, row, 0.2)) {
			return end;
		}
		lineNum = 0;
		seperator.clear();
		for (int y = h - 1; y > 0; y--) {
			int up = 0;
			int down = 0;
			for (int i = y; i > 0; i--) {
				if (row[i] > 10) {
					y = i - 1;
					down = i + 5 < h - 1 ? i + 5 : h - 1;
					break;
				}
				if (i == 0) {
					y = 0;
				}
			}
			for (int i = y; i > 0; i--) {
				if (row[i] < 10) {
					y = i;
					up = i - 5 > 0 ? i - 5 : 0;
					break;
				}
				if (i == 1) {
					y = 0;
				}
			}
			if ((down - up > lineH) && (down - up < h * 0.35D)) {
				seperator.add(Integer.valueOf(up));
				seperator.add(Integer.valueOf(down));

				lineImg = img.getSubimage(0, up, w, down - up);
				lineNum++;
				File imgFile = new File(System.currentTimeMillis() + ".png");
				try {
					ImageIO.write(lineImg, "png", imgFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				txt = OCR.recognizeText(imgFile, "-psm 7");

				if (txt.isEmpty()) {
					txt = OCR.recognizeText(imgFile, "");
				}
				System.out.println("2-" + txt);
				imgFile.delete();
				if (txt.matches(".{0,2}[此北]致.{0,5}|具状.{2,6}|答[辨辩].*|谢谢法庭.*"
						+ "|.{1,9}[耳年].{1,3}[月目用门].{1,4}[口日曰白门].{0,4}|.{3,10}法[阮院脘][^起诉]{0,4}"
						+ "|.{2,6}[级区]人民法.{1,2}|.{0,1}附.{0,1}|.*光盘.张.{0,2}|[\\d].{0,4}卷宗.{2,4}"
						+ "|.*起诉人:.*|[此北].{1,3}|.{0,2}[上公]诉[人八]:.{1,4}|[申巾]请[人八]:.{0,3}|附:.*"
						+ "|.*法律服务所|.*律[师帅]事务所|.{1,10}律师|.?[窜审]判[长员].{1,5}|被代:.*|原代:.*"
						+ "|负责人：.*|.?社区矫正对.{2,6}|[辨辩]护[人儿].{2,4}|年月[日曰门]|谢谢|检察员:."
						+ "|.{3,10}人[民尺].?[院浣脘][^起诉]{0,7}|.{2,5}司.局|.{3,9}挥部"
						+ "|.{2,8}办公室.{1,6}[耳年].{0,3}[月目用门].{0,4}[口日曰白门].{0,4}|.{0,4}市.{2,7}民法.?"
						+ "|.{1,6}[耳年].{0,3}[月目用门].?[\\d].{0,2}|[\\d]{3,5}年.{2,7}"
						+ "|.{1,3}[市币].{1,3}区人民.{1,5}")) {
					return end;
				}

			}
			if (lineNum > 4) {
				break;
			}
		}
		return "";
	}

	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		File dir = new File("test");
		BufferedImage img = null;
		for (File file : dir.listFiles()) {
			System.out.println(file.getName());
			try {
				img = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			img = Main.removeStamp(img);
			String result = check(img);
			System.out.println(result);

		}
		long time2 = System.currentTimeMillis();
		System.out.println(time2 - time1);
	}
}