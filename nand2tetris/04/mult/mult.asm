// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[3], respectively.)



@2  //go to final ans box
M=0 //zero ans 

@0
D=M
@END
D;JEQ   //If one product is zero

@1
D=M
@END
D;JEQ   //If one product is zero

@0  //Not necessary
D=M //
@3  //
M=D //Only to keep numbers being multiplied to save to mem


(LOOP)
@1  //Get 2nd num
D=M //D now has 2nd num

@2  //Go to final ans box
M=D+M   //RAM[2] has 2nd num and value

@3      //Get 1st num
M=M-1   //1ST NUM-1

D=M     //
@LOOP   //Where to jump to
D;JGT   //Jump      


(END)
@END
0;JMP   
//Forever loop