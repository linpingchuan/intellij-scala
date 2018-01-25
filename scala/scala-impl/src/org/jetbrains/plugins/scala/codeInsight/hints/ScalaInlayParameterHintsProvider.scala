package org.jetbrains.plugins.scala.codeInsight.hints

import java.{util => ju}

import com.intellij.codeInsight.hints._
import com.intellij.psi.PsiElement
import org.jetbrains.plugins.scala.ScalaLanguage
import org.jetbrains.plugins.scala.lang.psi.api.expr.{ScMethodCall, ScReferenceExpression}
import org.jetbrains.plugins.scala.lang.psi.api.statements.ScFunction

import scala.collection.JavaConverters

class ScalaInlayParameterHintsProvider extends InlayParameterHintsProvider {

  override def getParameterHints(element: PsiElement): ju.List[InlayInfo] =
    element match {
      case ScMethodCall(ScReferenceExpression(function: ScFunction), arguments) =>
        import JavaConverters._
        function.parameters
          .zip(arguments)
          .map {
            case (parameter, argument) => new InlayInfo(parameter.name, argument.getTextRange.getStartOffset)
          }.toList.asJava
      case _ => ju.Collections.emptyList()
    }

  override def getHintInfo(element: PsiElement): HintInfo = null

  override def getDefaultBlackList: ju.Set[String] = ju.Collections.emptySet()

  override def canShowHintsWhenDisabled: Boolean = true
}

object ScalaInlayParameterHintsProvider {

  def instance: ScalaInlayParameterHintsProvider =
    InlayParameterHintsExtension.INSTANCE.forLanguage(ScalaLanguage.INSTANCE) match {
      case provider: ScalaInlayParameterHintsProvider => provider
    }
}