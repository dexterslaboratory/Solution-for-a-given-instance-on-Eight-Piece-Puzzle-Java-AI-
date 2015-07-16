/*
 * 8-piece problem
 * author: Sidddharth Soni
 */

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class EightPiecePuzzle {
	public static Queue<state> q = new LinkedList<state>();
	public static LinkedList<state> visited = new LinkedList<state>();
	public static void main(String args[]) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int s[][] = new int[3][3];
		// ----take input-------
		System.out.println("Enter start state:");
		for (int i = 0; i < 3; i++) {
			String input[] = br.readLine().split(" ");
			for (int j = 0; j < 3; j++) {
				s[i][j] = Integer.parseInt(input[j]);
			}
		}
		// -----input taken------
		state initial = new state(s);
		//printState(initial);
		visited.add(initial);
		q.add(initial);
		ai();
	}
	public static void ai() {
		state s = q.poll();
		//evaluate each state in the queue using heuristic function
		//select the state with max heuristic ..call ai for that state after
		generateStatesAndEnque(s);
		state nextState = findMinHeuristicState();
		//the q will have only the state with max h
		//System.out.println("state selected");
		System.out.println("-------Next State:-------");
		printState(nextState);
		System.out.println("-------------------------");
		if(manhattanDistance(nextState)==0){
			System.out.println("Solution complete");
		}
		else{
		ai();}
	}
	public static void printState(state s){
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				System.out.print(s.s[i][j]+" ");
			}System.out.println();
		}
	}
	public static state findMinHeuristicState(){
		//compute heuristic value for each state in the queue
		//then return the state that has max heuristic value
		state minHeuristic = q.poll();
		int h = manhattanDistance(minHeuristic);
		for(state s:q){
			if(manhattanDistance(s)<h){
				h=manhattanDistance(s);
				minHeuristic = s;
			}
			else if(manhattanDistance(s)==h){
				//System.out.println("There are two states with same heuristic value");
				//printState(s);
				//printState(minHeuristic);
				//System.out.println("-------------------------");
			}
			else{
				//if the manhattan distance of state is more then do nothing
			}
		}
		q.clear();
		//after the state with max heuristic has been found add it to the q(which must be empty now)
		q.add(minHeuristic);
		return minHeuristic;
	}
	
	public static int manhattanDistance(state s){
		int h = 0;
		int[][] goalState = {{1,2,3},{8,0,4},{7,6,5}};
		for(int i=1;i<9;i++){//*****do not include the blank's(in this case 0) x nd y in the computation of manhattan distance******
			h += Math.abs(getXY(s.s,i,'x')-getXY(goalState,i,'x')) + Math.abs(getXY(s.s,i,'y')-getXY(goalState,i,'y'));
		}
		return h;
	}
	public static int getXY(int[][] s,int a,char c){
		for(int y=0;y<3;y++){
			for(int x=0;x<3;x++){
				if(s[x][y]==a){
					if(c=='x'){return x;}
					else if(c=='y'){return y;}
				}
			}
		}
		return 0;
	}

	public static void generateStatesAndEnque(state s) {//check this function 
		int check[][] = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
		int ZerosXnY[] = findZerosXY(s);
		int zeroX = ZerosXnY[0];
		int zeroY = ZerosXnY[1];
		//System.out.println("zero at "+zeroX+","+zeroY);
		//System.out.println("---Possibilities of ");
		//printState(s);
		//System.out.println("----------------------");
		boolean thereIsAtleastOneUnvisited = false;
		for (int i = 0; i < 4; i++) {
			int checkX = check[i][0];
			int checkY = check[i][1];
			int toSwapWithX = zeroX + checkX;
			int toSwapWithY = zeroY + checkY;
			//int nextState[][] =s.s.clone();//----
			int nextState[][] =copyArray(s.s);
			
			if (toSwapWithX < 3 && toSwapWithX > -1 && toSwapWithY < 3
					&& toSwapWithY > -1) {
				int temp = nextState[toSwapWithX][toSwapWithY];
				nextState[toSwapWithX][toSwapWithY] = nextState[zeroX][zeroY];
				nextState[zeroX][zeroY] = temp;
				state nexts = new state(nextState);
				//System.out.println("new state after swap");
				//printState(nexts);
				if (!isVisited(nexts)) {
					q.add(nexts);
					visited.add(nexts);
					thereIsAtleastOneUnvisited = true;
					//System.out.println("here");
					//printState(nexts);
					//System.out.println("------------");
				}
			}
		}
		if(thereIsAtleastOneUnvisited==false){System.out.println("**No Solution is Possible**");
												System.exit(0);}
	}
	public static int[][] copyArray(int c[][]){
		int copy[][] = new int[3][3];
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				copy[i][j]=c[i][j];
			}
		}
		return copy;
	}
	public static int[] findZerosXY(state s) {
		int XnY[] = new int[2];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (s.s[i][j] == 0) {
					XnY[0] = i;
					XnY[1] = j;
					break;
				}
			}
		}
		return XnY;
	}

	public static boolean isVisited(state toCheckIfVisited) {
		for (state s : visited) {
			int counter = 0;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (toCheckIfVisited.s[i][j] == s.s[i][j]) {
						counter++;
					}
				}
			}
			if (counter==9)return true;
		}
		return false;
	}

	static class state {
		int s[][] = new int[3][3];
		state(int s[][]) {
			this.s = s;
		}
	}
}
