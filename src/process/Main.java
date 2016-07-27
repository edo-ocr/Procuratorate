package process;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main implements FileCode {

	public static void main(String[] args) {
		File file = new File(args[0]);
		BufferedImage img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String result = Document.check(img);
		System.out.println(result);
		switch (result) {
		
		case end:
			writeFile(args[1], end);
			System.out.println(file.getName() + "——文件尾页");
			break;
		case fileCover:
			writeFile(args[1], fileCover);
			System.out.println(file.getName() + "——诉讼文书卷案封面");
			break;
		case index:
			writeFile(args[1], index);
			System.out.println(file.getName() + "——卷内目录");
			break;
		case indexFront:
			writeFile(args[1], indexFront);
			System.out.println(file.getName() + "——首页——卷内目录");
			break;
		case receiveRegiForm:
			writeFile(args[1], receiveRegiForm);
			System.out.println(file.getName() + "——接受刑事案件登记表、受案登记表");
			break;
		case transferCaseNotification:
			writeFile(args[1], transferCaseNotification);
			System.out.println(file.getName() + "——移交案件通知书");
			break;
		case fileCaseDecision:
			writeFile(args[1], fileCaseDecision);
			System.out.println(file.getName() + "——立案决定书");
			break;
		case summonNotification:
			writeFile(args[1], summonNotification);
			System.out.println(file.getName() + "——传唤通知书、拘传证");
			break;
		case searchWarrant:
			writeFile(args[1], searchWarrant);
			System.out.println(file.getName() + "——搜查证");
			break;
		case distrainList:
			writeFile(args[1], distrainList);
			System.out.println(file.getName() + "——扣押决定书、扣押物品、文件清单");
			break;
		case detentionNotification:
			writeFile(args[1], detentionNotification);
			System.out.println(file.getName() + "——拘留通知书、拘留证、延长拘留期限通知书");
			break;
		case arrestApplication:
			writeFile(args[1], arrestApplication);
			System.out.println(file.getName() + "——提请批准逮捕书");
			break;
		case arrestNotification:
			writeFile(args[1], arrestNotification);
			System.out.println(file.getName() + "——逮捕通知书、逮捕证");
			break;
		case lengthenInveNotification:
			writeFile(args[1], lengthenInveNotification);
			System.out.println(file.getName() + "——批准或不批准延长侦查羁押期限决定书");
			break;
		case releaseNotification:
			writeFile(args[1], releaseNotification);
			System.out.println(file.getName() + "——变更强制措施通知书、释放通知书、释放证明书");
			break;
		case bailPendingTrial:
			writeFile(args[1], bailPendingTrial);
			System.out.println(file.getName() + "——取保候审决定书、取保候审保证书或收取保证金通知书");
			break;
		case prosecutionProposal:
			writeFile(args[1], prosecutionProposal);
			System.out.println(file.getName() + "——起诉意见书、案件起诉告知书");
			break;
		case other:
			writeFile(args[1], other);
			System.out.println(file.getName() + "——其他");
			break;
		case evidenceCover:
			writeFile(args[1], evidenceCover);
			System.out.println(file.getName() + "——证据材料封面");
			break;
		default:
			writeFile(args[1], none);
			System.out.println(file.getName() + "——无特定分类");
			break;
		}
		

	}

	public static void writeFile(String path, String content) {
		File file = new File(path);
		try {
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage removeStamp(BufferedImage img) {
		int size = img.getHeight() / 12;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				img.setRGB(x, y, Color.white.getRGB());
			}
		}
		return img;
	}

}
