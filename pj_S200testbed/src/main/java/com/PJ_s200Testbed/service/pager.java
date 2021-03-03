package com.PJ_s200Testbed.service;

public class pager {
	public static final int PAGE_SCALE=10; // 페이지당 게시물수
	public static final int BLOCK_SCALE=10; // 화면당 페이지 수
	private int curPage; //현재 페이지
	private int prevPage; //이전 페이지
	private int nextPage; //다음 페이지
	private int totPage; // 전체 페이지 갯수
	private int totBlock; //전체 페이지블록 갯수
	private int curBlock; //현재 블록
	private int prevBlock; //이전 블록
	private int nextBlock; // 다음 불록
	private int pageBegin; // #{start} 변수에 전달될 값
	private int pageEnd; // #{end} 변수에 전달될 값
	private int blockBegin; // 블록의 시작페이지 번호
	private int blockEnd; // 블록의 끝페이지 번호 
	
	//Pager(레코드갯수, 출력할페이지 번호)
	public pager(int count, int curPage) { // 1. count 에는 15 curpage  에는 1이 들어온 상황
		curBlock = 1; //  현재 블록 번호 
		this.curPage = curPage; // 현재 페이지 번호 // 2. 전역변수 curpage에 1을 넣어라 
		setTotPage(count); //현재 페이지번호  // 6. 2라는 값이 들어온다
		setPageRange(); // #{start}, #{end}값 계산
		setTotBlock(); // 전체 블록 갯수 계산 
		setBlockRange(); // 블록의 시작, 끝 번호 계산
	} // 생성자 계산이 끝나면 다시 boardController에 pager에 값이 들어간다 
	
	public void setBlockRange() {
		//원하는 페이지가 몇번째 블록에 속하는지 계산
		curBlock= (curPage-1)/BLOCK_SCALE+1;
		//블록의 시작페이지, 끝페이지 번호 계산
		blockBegin= (curBlock-1)*BLOCK_SCALE+1;
		blockEnd=blockBegin+BLOCK_SCALE-1;
		//마지막 블록 번호가 범위를 초과하지 않도록 처리 
		if(blockEnd > totPage) {
			blockEnd = totPage;
		}
		//[이전][다음]을 눌렀을때 이동할 페이지 번호
		// 3항 연산자 ? 기준으로 -> true ? false
		prevPage=(curBlock==1) ? 1 : (curBlock-1)*BLOCK_SCALE;
		nextPage=curBlock>totBlock ? (curBlock*BLOCK_SCALE)
				: (curBlock*BLOCK_SCALE)+1;
		//마지막 페이지가 범위를 초과하지 않도록 처리
		if(nextPage >= totPage) {
			nextPage=totPage;
		}
	}
	//블록의 갯수 계산
	public void setTotBlock() {
		totBlock = (int)Math.ceil(totPage*1.0 / BLOCK_SCALE);
	}
	//where rn between #{start} and #{end}에 입력될 값
	public void setPageRange() {
		//시작번호=(현재페이지-1)x 페이지당 게시물수 + 1
		// 끝번호=시작번호 + 페이지당 게시물 수 -1
			pageBegin = (curPage-1) * PAGE_SCALE + 1; // 7. curpage : 2, page_scale: 10 
			pageEnd = pageBegin + PAGE_SCALE -1; // 8. 결과값 10 
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getTotPage() {
		return totPage;
	}

	public void setTotPage(int count) { //3. count에 15가 들어옴 
		//MATH.ceil() 올림
		totPage = (int)Math.ceil(count*1.0 / PAGE_SCALE);// 4. 15.0 / 10  
	}													 // 5. 1.5를 ceill로 올림 하게 되므로 2라는 결과값이 나온다 

	public int getTotBlock() {
		return totBlock;
	}

	public void setTotBlock(int totBlock) {
		this.totBlock = totBlock;
	}

	public int getCurBlock() {
		return curBlock;
	}

	public void setCurBlock(int curBlock) {
		this.curBlock = curBlock;
	}

	public int getPrevBlock() {
		return prevBlock;
	}

	public void setPrevBlock(int prevBlock) {
		this.prevBlock = prevBlock;
	}

	public int getNextBlock() {
		return nextBlock;
	}

	public void setNextBlock(int nextBlock) {
		this.nextBlock = nextBlock;
	}

	public int getPageBegin() {
		return pageBegin;
	}

	public void setPageBegin(int pageBegin) {
		this.pageBegin = pageBegin;
	}

	public int getPageEnd() {
		return pageEnd;
	}

	public void setPageEnd(int pageEnd) {
		this.pageEnd = pageEnd;
	}

	public int getBlockBegin() {
		return blockBegin;
	}

	public void setBlockBegin(int blockBegin) {
		this.blockBegin = blockBegin;
	}

	public int getBlockEnd() {
		return blockEnd;
	}

	public void setBlockEnd(int blockEnd) {
		this.blockEnd = blockEnd;
	}
	

}
