package com.example.Util;

/**
 * �ɸ�Э�鳣��
 * @author ccf
 * 2012/2/10
 */
public class IpMessageConst {
	public static final int VERSION = 0x001;		// �汾��
	public static final int PORT = 0x0979;			// �˿ںţ��ɸ�Э��Ĭ�϶˿�2425
	
	// ����
	public static final int IPMSG_NOOPERATION		 = 0x00000000;	//�������κβ���
	public static final int IPMSG_BR_ENTRY			 = 0x00000001;	//�û�����
	public static final int IPMSG_BR_EXIT		 	 = 0x00000002;	//�û��˳�
	public static final int IPMSG_ANSENTRY			 = 0x00000003;	//ͨ������
	public static final int IPMSG_BR_ABSENCE		 = 0x00000004;	//��Ϊȱϯģʽ
	
	public static final int IPMSG_BR_ISGETLIST		 = 0x00000010;	//Ѱ����Ч�Ŀ��Է����û��б�ĳ�Ա
	public static final int IPMSG_OKGETLIST			 = 0x00000011;	//֪ͨ�û��б��Ѿ����
	public static final int IPMSG_GETLIST			 = 0x00000012;	//�û��б�������
	public static final int IPMSG_ANSLIST			 = 0x00000013;	//Ӧ���û��б�������
	public static final int IPMSG_FILE_MTIME		 = 0x00000014;	//
	public static final int IPMSG_FILE_CREATETIME	 = 0x00000016;	//
	public static final int IPMSG_BR_ISGETLIST2		 = 0x00000018;	//
	
	public static final int IPMSG_SENDMSG 			 = 0x00000020;	//������Ϣ
	public static final int IPMSG_RECVMSG 			 = 0x00000021;	//ͨ���յ���Ϣ
	public static final int IPMSG_READMSG 			 = 0x00000030;	//��Ϣ��֪ͨ
	public static final int IPMSG_DELMSG 			 = 0x00000031;	//��Ϣ����֪ͨ
	public static final int IPMSG_ANSREADMSG		 = 0x00000032;	//��Ϣ��ȷ��֪ͨ��version-8����ӣ�
	
	public static final int IPMSG_GETINFO			 = 0x00000040;	//���IPMSG�汾��Ϣ
	public static final int IPMSG_SENDINFO			 = 0x00000041;	//����IPMSG�汾��Ϣ
	
	public static final int IPMSG_GETABSENCEINFO	 = 0x00000050;	//���ȱϯ��Ϣ
	public static final int IPMSG_SENDABSENCEINFO	 = 0x00000051;	//����ȱϯ��Ϣ
	
	public static final int IPMSG_GETFILEDATA		 = 0x00000060;	//�ļ���������
	public static final int IPMSG_RELEASEFILES		 = 0x00000061;	//���������ļ�
	public static final int IPMSG_GETDIRFILES		 = 0x00000062;	//����ͳ���ļ�����
	
	public static final int IPMSG_GETPUBKEY			 = 0x00000072;	//���RSA��Կ
	public static final int IPMSG_ANSPUBKEY			 = 0x00000073;	//Ӧ��RSA��Կ
	
	/* option for all command */
	public static final int IPMSG_ABSENCEOPT 		 = 0x00000100;	//ȱϯģʽ
	public static final int IPMSG_SERVEROPT 		 = 0x00000200;	//��������������
	public static final int IPMSG_DIALUPOPT 		 = 0x00010000;	//���͸�����
	public static final int IPMSG_FILEATTACHOPT 	 = 0x00200000;	//�����ļ�
	public static final int IPMSG_ENCRYPTOPT		 = 0x00400000;	//����
	
	/*  option for send command  */
	public static final int IPMSG_SENDCHECKOPT = 0x00000100;	//������֤
	public static final int IPMSG_SECRETOPT = 0x00000200;		//�ܷ����Ϣ
	public static final int IPMSG_BROADCASTOPT = 0x00000400;	//�㲥
	public static final int IPMSG_MULTICASTOPT = 0x00000800;	//�ಥ
	public static final int IPMSG_NOPOPUPOPT = 0x00001000;		//��������Ч��
	public static final int IPMSG_AUTORETOPT = 0x00002000;		//�Զ�Ӧ��(Ping-pong protection)
	public static final int IPMSG_RETRYOPT = 0x00004000;		//�ط���ʶ�����������û��б�ʱ��
	public static final int IPMSG_PASSWORDOPT = 0x00008000;		//����
	public static final int IPMSG_NOLOGOPT = 0x00020000;		//û����־�ļ�
	public static final int IPMSG_NEWMUTIOPT = 0x00040000;		//�°汾�Ķಥ��������
	public static final int IPMSG_NOADDLISTOPT = 0x00080000;	//������û��б� Notice to the members outside of BR_ENTRY
	public static final int IPMSG_READCHECKOPT = 0x00100000;	//�ܷ���Ϣ��֤��version8����ӣ�
	public static final int IPMSG_SECRETEXOPT = (IPMSG_READCHECKOPT|IPMSG_SECRETOPT);
	
	/* encryption flags for encrypt command */
	public static final int IPMSG_RSA_512 			 = 0x00000001;
	public static final int IPMSG_RSA_1024 			 = 0x00000002;
	public static final int IPMSG_RSA_2048 			 = 0x00000004;
	public static final int IPMSG_RC2_40 			 = 0x00001000;
	public static final int IPMSG_RC2_128 			 = 0x00004000;
	public static final int IPMSG_RC2_256 			 = 0x00008000;
	public static final int IPMSG_BLOWFISH_128 			 = 0x00020000;
	public static final int IPMSG_BLOWFISH_256 			 = 0x00040000;
	public static final int IPMSG_SIGN_MD5 			 = 0x10000000;
	
	/* file types for fileattach command */
	public static final int IPMSG_FILE_REGULAR 			 = 0x00000001;
	public static final int IPMSG_FILE_DIR 			 = 0x00000002;
	public static final int IPMSG_FILE_RETPARENT 			 = 0x00000003;	// return parent directory
	public static final int IPMSG_FILE_SYMLINK 			 = 0x00000004;
	public static final int IPMSG_FILE_CDEV 			 = 0x00000005;	// for UNIX
	public static final int IPMSG_FILE_BDEV 			 = 0x00000006;	// for UNIX
	public static final int IPMSG_FILE_FIFO 			 = 0x00000007;	// for UNIX
	public static final int IPMSG_FILE_RESFORK 			 = 0x00000010;	// for Mac

	/* file attribute options for fileattach command */
	public static final int IPMSG_FILE_RONLYOPT 			 = 0x00000100;
	public static final int IPMSG_FILE_HIDDENOPT 			 = 0x00001000;
	public static final int IPMSG_FILE_EXHIDDENOPT 			 = 0x00002000;	// for MacOS X
	public static final int IPMSG_FILE_ARCHIVEOPT 			 = 0x00004000;
	public static final int IPMSG_FILE_SYSTEMOPT 			 = 0x00008000;

	/* extend attribute types for fileattach command */
	public static final int IPMSG_FILE_UID 				 = 0x00000001;
	public static final int IPMSG_FILE_USERNAME 		 = 0x00000002;	// uid by string
	public static final int IPMSG_FILE_GID 				 = 0x00000003;
	public static final int IPMSG_FILE_GROUPNAME 		 = 0x00000004;	// gid by string
	public static final int IPMSG_FILE_PERM 			 = 0x00000010;	// for UNIX
	public static final int IPMSG_FILE_MAJORNO 			 = 0x00000011;	// for UNIX devfile
	public static final int IPMSG_FILE_MINORNO 			 = 0x00000012;	// for UNIX devfile
	public static final int IPMSG_FILE_CTIME 			 = 0x00000013;	// for UNIX
	public static final int IPMSG_FILE_ATIME 			 = 0x00000015;
	public static final int IPMSG_FILE_CREATOR 			 = 0x00000020;	// for Mac
	public static final int IPMSG_FILE_FILETYPE 		 = 0x00000021;	// for Mac
	public static final int IPMSG_FILE_FINDERINFO 		 = 0x00000022;	// for Mac
	public static final int IPMSG_FILE_ACL 				 = 0x00000030;
	public static final int IPMSG_FILE_ALIASFNAME 		 = 0x00000040;	// alias fname
	public static final int IPMSG_FILE_UNICODEFNAME 	 = 0x00000041;	// UNICODE fname
	

}
