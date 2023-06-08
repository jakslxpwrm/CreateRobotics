//> Appendix II stmt
package com.workert.robotics.roboscript;

import java.util.List;

public abstract class Statement {
	interface Visitor<R> {
		R visitBlockStmt(Block stmt);

		R visitClassStmt(Class stmt);

		R visitExpressionStmt(Expression stmt);

		R visitFunctionStmt(Function stmt);

		R visitIfStmt(If stmt);

		R visitReturnStmt(Return stmt);

		R visitBreakStmt(Break stmt);

		R visitVarStmt(Var stmt);

		R visitWhileStmt(While stmt);
	}

	// Nested Stmt classes here...
	//> stmt-block
	static class Block extends Statement {
		Block(List<Statement> statements) {
			this.statements = statements;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitBlockStmt(this);
		}

		final List<Statement> statements;
	}

	//< stmt-block
	//> stmt-class
	static class Class extends Statement {
		Class(Token name, com.workert.robotics.roboscript.Expression.Variable superclass, List<Statement.Function> methods) {
			this.name = name;
			this.superclass = superclass;
			this.methods = methods;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitClassStmt(this);
		}

		final Token name;
		final com.workert.robotics.roboscript.Expression.Variable superclass;
		final List<Statement.Function> methods;
	}

	//< stmt-class
	//> stmt-expression
	static class Expression extends Statement {
		Expression(com.workert.robotics.roboscript.Expression expression) {
			this.expression = expression;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitExpressionStmt(this);
		}

		final com.workert.robotics.roboscript.Expression expression;
	}

	//< stmt-expression
	//> stmt-function
	static class Function extends Statement {
		Function(Token name, List<Token> params, List<Statement> body) {
			this.name = name;
			this.params = params;
			this.body = body;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitFunctionStmt(this);
		}

		final Token name;
		final List<Token> params;
		final List<Statement> body;
	}

	//< stmt-function
	//> stmt-if
	static class If extends Statement {
		If(com.workert.robotics.roboscript.Expression condition, Statement thenBranch, Statement elseBranch) {
			this.condition = condition;
			this.thenBranch = thenBranch;
			this.elseBranch = elseBranch;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitIfStmt(this);
		}

		final com.workert.robotics.roboscript.Expression condition;
		final Statement thenBranch;
		final Statement elseBranch;
	}

	//< stmt-if
	//> stmt-return
	static class Return extends Statement {
		Return(Token keyword, com.workert.robotics.roboscript.Expression value) {
			this.keyword = keyword;
			this.value = value;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitReturnStmt(this);
		}

		final Token keyword;
		final com.workert.robotics.roboscript.Expression value;
	}

	//< stmt-return
	//> stmt-break
	static class Break extends Statement {
		Break(Token keyword) {
			this.keyword = keyword;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitBreakStmt(this);
		}

		final Token keyword;
	}

	//< stmt-break
	//> stmt-var
	static class Var extends Statement {
		Var(Token name, com.workert.robotics.roboscript.Expression initializer) {
			this.name = name;
			this.initializer = initializer;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitVarStmt(this);
		}

		final Token name;
		final com.workert.robotics.roboscript.Expression initializer;
	}

	//< stmt-var
	//> stmt-while
	static class While extends Statement {
		While(com.workert.robotics.roboscript.Expression condition, Statement body) {
			this.condition = condition;
			this.body = body;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitWhileStmt(this);
		}

		final com.workert.robotics.roboscript.Expression condition;
		final Statement body;
	}
	//< stmt-while

	abstract <R> R accept(Visitor<R> visitor);
}
//< Appendix II stmt