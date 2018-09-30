// Created by Lukas Mansour on the 2018-09-26 at 17:02:13
// This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivative International License. (Short Code: CC BY-NC-ND 4.0 )
// To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/
// Or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

package de.articdive.townyeco.objects.trade_objects;

import de.articdive.townyeco.objects.trade_objects.abstractions.TETradeObject;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("experience")
public class TEExperienceTradeObject extends TETradeObject {

	@SuppressWarnings("unused")
	private TEExperienceTradeObject() {
		super();
	}
}
