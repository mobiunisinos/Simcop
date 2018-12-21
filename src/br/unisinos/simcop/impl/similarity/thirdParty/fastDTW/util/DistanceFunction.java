/*
 * Arrays.java   Jul 14, 2004
 *
 * Copyright (c) 2004 Stan Salvador
 * stansalvador@hotmail.com
 */

package br.unisinos.simcop.impl.similarity.thirdParty.fastDTW.util;


public interface DistanceFunction
{
   public double calcDistance(double[] vector1, double[] vector2);
}