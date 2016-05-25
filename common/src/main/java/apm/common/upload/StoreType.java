package apm.common.upload;

/**
 * 文件存储类型
 * <p>
 * 
 * @author Zaric
 *         <p>
 * @date 2013-9-9 上午10:03:11
 */
public enum StoreType {
	
	/**
	 * 返回项目路径
	 */
	FOLDER {
		@Override
		public int getValue() {
			return 1;
		}

		@Override
		public String getName() {
			return "Folder";
		}
	},
	
	/**
	 * 返回系统路径
	 */
	SYSTEM {
		@Override
		public int getValue() {
			return 2;
		}

		@Override
		public String getName() {
			return "System";
		}
	},

	/**
	 * 返回营业执照
	 */
	LICENCE {
		@Override
		public int getValue() {
			return 3;
		}

		@Override
		public String getName() {
			return "Licence";
		}
	},
	
	/**
	 * 返回法定代表人身份证
	 */
	CERTIFICATE {
		@Override
		public int getValue() {
			return 4;
		}

		@Override
		public String getName() {
			return "Certificate";
		}
	},
	
	/**
	 * 返回组织机构代码证
	 */
	ORGAN {
		@Override
		public int getValue() {
			return 5;
		}

		@Override
		public String getName() {
			return "Organ";
		}
	},
	
	/**
	 * 返回其他
	 */
	OTHERS {
		@Override
		public int getValue() {
			return 0;
		}

		@Override
		public String getName() {
			return "Others";
		}
	};
	
	public abstract int getValue();

	public abstract String getName();
}